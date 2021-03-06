package spiaa.controller.api;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spiaa.model.ServiceLocator;
import spiaa.model.entity.Usuario;

/**
 *
 * @author William
 */
@Controller
//@RequestMapping(value = "/api/agente")
public class UsuarioApiController {

    @RequestMapping(value = "/api/agente/login", method = RequestMethod.POST)
    public @ResponseBody
    Usuario login(@RequestBody Usuario agenteSaude, HttpServletResponse response) {
        Usuario agente = null;
        try {
            agente = ServiceLocator.getbaseAgenteSaudeService()
                    .login(agenteSaude.getUsuario(), agenteSaude.getSenha());
            response.setStatus(200);

        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
        }
        return agente;
    }

    @RequestMapping(value = "/api/agente/alterar", method = RequestMethod.POST)
    public @ResponseBody
    String alterarUsuario(@RequestBody Usuario usuario) {
        String resposta = "SUCCESS";

        try {
            if (usuario != null && usuario.getId() > 0) {
                ServiceLocator.getBaseUsuarioService().update(usuario);
            } else {
                resposta = "ERROR - id = null";
            }
        } catch (Exception e) {
            resposta = "ERROR";
            e.printStackTrace();
        }
        return resposta;
    }
}
