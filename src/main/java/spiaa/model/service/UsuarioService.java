package spiaa.model.service;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import spiaa.model.ConnectionManager;
import spiaa.model.ServiceLocator;
import spiaa.model.base.service.BaseUsuarioService;
import spiaa.model.dao.UsuarioDAO;
import spiaa.model.entity.Parametros;
import spiaa.model.entity.RecuperarSenha;
import spiaa.model.entity.Usuario;

public class UsuarioService implements BaseUsuarioService {

    @Override
    public void create(Usuario entity) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            String senha = encodeStrToUTF8(entity.getSenha());
            entity.setSenha(senha);
            dao.create(entity, conn);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
        }
    }

    @Override
    public Usuario readById(Long id) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        Usuario usuario = null;
        UsuarioDAO dao = new UsuarioDAO();
        usuario = dao.readById(id, conn);
        conn.close();
        return usuario;
    }

    @Override
    public List<Usuario> readByCriteria(Map<String, Object> criteria) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        List<Usuario> listUsuario = new ArrayList<>();
        UsuarioDAO dao = new UsuarioDAO();
        listUsuario = dao.readByCriteria(criteria, conn);
        conn.close();
        return listUsuario;
    }

    @Override
    public void update(Usuario entity) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            entity.setSenha(encodeStrToUTF8(entity.getSenha()));
            dao.update(entity, conn);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.delete(id, conn);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
        }
    }

    @Override
    public Usuario login(String usuario, String senha) throws Exception {
        Usuario usuarioLogado = null;
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            UsuarioDAO dao = new UsuarioDAO();
            Map<String, Object> criteria = new HashMap<>();
            senha = encodeStrToUTF8(senha);
            criteria.put(UsuarioDAO.CRITERION_USUARIO_EQ, usuario);
            criteria.put(UsuarioDAO.CRITERION_SENHA_EQ, senha);
            List<Usuario> usuarioList = dao.readByCriteria(criteria, conn);
            if (usuarioList != null && usuarioList.size() == 1) {
                usuarioLogado = usuarioList.get(0);
                if (!usuarioLogado.getUsuario().equals(usuario)) {
                    usuarioLogado = null;
                } else {
                    if (!usuarioLogado.getSenha().equals(senha)) {
                        usuarioLogado = null;
                    } else if (!usuarioLogado.getTipo().equalsIgnoreCase(UsuarioDAO.TIPO_ADMNISTRADOR)) {
                        usuarioLogado = null;
                    }
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return usuarioLogado;
    }

    public String encodeStrToUTF8(String obj) {
        String hash = obj + "spiaaweb2015";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(hash.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public void recuperarSenhaCreate(RecuperarSenha recuperar) throws Exception {

        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            String token = encodeStrToUTF8(recuperar.getUsuario().getEmail() + new Date().toString());
            recuperar.setToken(token);

            UsuarioDAO dao = new UsuarioDAO();
            dao.recuperarSenhaCreate(recuperar, conn);
            //envia email
            enviarEmailRecuperarSenha(recuperar);

            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
        }
    }

    @Override
    public void recuperarSenhaDelete(Long id) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.recuperarSenhaDelete(id, conn);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
        }
    }

    @Override
    public RecuperarSenha recuperarSenhaReadByUserToken(String usuario, String token) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        RecuperarSenha recuperarSenha = new RecuperarSenha();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            recuperarSenha = dao.recuperarSenhaReadByUserToken(usuario, token, conn);
            conn.close();
        } catch (Exception e) {
            conn.close();
            e.printStackTrace();
        }
        return recuperarSenha;
    }

    @Override
    public void enviarEmailRecuperarSenha(RecuperarSenha recuperarSenha) throws Exception {
        Map<String, Object> criteria = new HashMap<>();
        List<Parametros> parametrosList = ServiceLocator.getbaseParametrosService().readByCriteria(criteria);
        Parametros parametros = null;
        if (parametrosList != null && parametrosList.size() > 0) {
            parametros = parametrosList.get(0);
        } else {
            return;
        }

        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName(parametros.getSmtp());
        email.setSmtpPort(parametros.getPorta());
        email.setAuthenticator(new DefaultAuthenticator(parametros.getEmail(), parametros.getSenha()));
        email.setSSLOnConnect(true);
        email.addTo(recuperarSenha.getUsuario().getEmail(), recuperarSenha.getUsuario().getNome());
        email.setFrom(parametros.getEmail(), "SPIAA");
        email.setSubject("SPIAA - Recuperar Senha");

        URL url = new URL(parametros.getCaminhoHostName());
        String msg = "<html>";
        msg += "<p>Ola " + recuperarSenha.getUsuario().getNome() + "!</p>";
        msg += "<p>Clique no link para redefinir sua senha :<p/>";
        msg += "<a href='" + url + "/login/redefinirsenha?usuario=" + recuperarSenha.getUsuario().getEmail() + "&confirmacao=" + recuperarSenha.getToken() + "'> redefnir senha</a>";
        msg += "<p>Caso o link n&atildeo;o estiver habilitado copie o texto e cole no seu navegador :<p/>";
        msg += url + "/login/redefinirsenha?usuario=" + recuperarSenha.getUsuario().getEmail() + "&confirmacao=" + recuperarSenha.getToken();
        msg += "</html>";
        // set the html message
        email.setHtmlMsg(msg);
        // send the email
        email.send();
    }

    @Override
    public void redefinirSenha(RecuperarSenha recuperar) throws Exception {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            recuperar.getUsuario().setSenha(encodeStrToUTF8(recuperar.getUsuario().getSenha()));
            dao.redefinirSenha(recuperar, conn);
            recuperarSenhaDelete(recuperar.getId());
            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
            throw e;
        }
    }
}
