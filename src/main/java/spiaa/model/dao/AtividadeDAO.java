package spiaa.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import spiaa.model.base.BaseDAO;
import spiaa.model.entity.Atividade;
import spiaa.model.entity.AtividadeCriadouro;
import spiaa.model.entity.AtividadeInseticida;
import spiaa.model.entity.TratamentoAntiVetorial;
import spiaa.model.entity.Criadouro;
import spiaa.model.entity.Inseticida;
import spiaa.model.entity.Quarteirao;
import spiaa.model.entity.TipoImoveis;

public class AtividadeDAO implements BaseDAO<Atividade> {

    public static final String CRITERION_BOLETIM_ID_EQ = "1";
    public static final String CRITERION_LAT_LNG_OK = "2";
    public static final String CRITERION_BAIRRO_ID_EQ = "3";
    public static final String CRITERION_DATA_INICIAL_EQ = "4";
    public static final String CRITERION_DATA_FINAL_EQ = "5";

    @Override
    public void create(Atividade entity, Connection conn) throws Exception {

        String sql = "INSERT INTO atividade (endereco, numero, observacao, inspecionado, tipo_imovel_fk, tratamento_antivetorial_fk, quarteirao_fk, latitude, longitude, data_inicial, data_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id;";
        PreparedStatement ps = conn.prepareStatement(sql);

        int i = 0;

        ps.setString(++i, entity.getEndereco());
        ps.setString(++i, entity.getNumero());
        ps.setString(++i, entity.getObservacao());
        ps.setInt(++i, entity.getInspecionado());
        ps.setLong(++i, entity.getTipoImoveis().getId());
        ps.setLong(++i, entity.getBoletimDiario().getId());
        ps.setLong(++i, entity.getQuarteirao().getId());
        ps.setString(++i, entity.getLatitude());
        ps.setString(++i, entity.getLongitude());
        ps.setTimestamp(++i, new Timestamp(entity.getDataInicial().getTime()));
        ps.setTimestamp(++i, new Timestamp(entity.getDataFinal().getTime()));

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            entity.setId(rs.getLong("id"));
        }
        rs.close();
        ps.close();

        List<AtividadeCriadouro> atividadeCriadouroList = entity.getAtividadeCriadouroList();
        if (atividadeCriadouroList != null && atividadeCriadouroList.size() > 0) {
            String sqlCriadouro = "INSERT INTO atividade_criadouro(atividade_fk, criadouro_fk, quantidade)VALUES (?, ?, ?);";
            PreparedStatement psCriadouro = conn.prepareStatement(sqlCriadouro);
            for (AtividadeCriadouro atividadeCriadouro : atividadeCriadouroList) {
                psCriadouro.setLong(1, entity.getId());
                psCriadouro.setLong(2, atividadeCriadouro.getCriadouro().getId());
                psCriadouro.setInt(3, atividadeCriadouro.getQuantidadeCriadouro());
                psCriadouro.execute();
            }
            psCriadouro.close();
        }

        List<AtividadeInseticida> atividadeInseticidaList = entity.getAtividadeInseticidasList();
        if (atividadeInseticidaList != null && atividadeInseticidaList.size() > 0) {
            String sqlInseticida = "INSERT INTO atividade_inseticida(atividade_fk, inseticida_fk, quantidade)VALUES (?, ?, ?);";
            PreparedStatement psinseticida = conn.prepareStatement(sqlInseticida);
            for (AtividadeInseticida atividadeInseticida : atividadeInseticidaList) {
                psinseticida.setLong(1, entity.getId());
                psinseticida.setLong(2, atividadeInseticida.getInseticida().getId());
                psinseticida.setInt(3, atividadeInseticida.getQuantidadeInseticida());
                psinseticida.execute();
            }
            psinseticida.close();
        }

    }

    @Override
    public Atividade readById(Long id, Connection conn) throws Exception {
        Atividade atividade = null;
        String sql = " SELECT atividade .*,tipo_imovel.id as tipo_imovel_id, tipo_imovel.descricao as tipo_imovel_descricao, tipo_imovel.sigla as tipo_imovel_sigla, ";
        sql += "  quarteirao.id as quarteirao_id, quarteirao.descricao as quarteirao_descricao  ";
        sql += " FROM atividade  ";
        sql += "LEFT JOIN tipo_imovel on tipo_imovel.id = atividade.tipo_imovel_fk   ";
        sql += "LEFT JOIN quarteirao on quarteirao.id = atividade.quarteirao_fk  ";
        sql += "WHERE atividade.id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {

            atividade = new Atividade();
            atividade.setId(rs.getLong("id"));
            atividade.setEndereco(rs.getString("endereco"));
            Quarteirao quarteirao = new Quarteirao();
            quarteirao.setId(rs.getLong("quarteirao_id"));
            quarteirao.setDescricao(rs.getString("quarteirao_descricao"));
            atividade.setQuarteirao(quarteirao);
            atividade.setNumero(rs.getString("numero"));
            atividade.setObservacao(rs.getString("observacao"));
            atividade.setInspecionado(rs.getInt("inspecionado"));
            atividade.setLatitude(rs.getString("latitude"));
            atividade.setLongitude(rs.getString("longitude"));
            atividade.setDataInicial(rs.getTimestamp("data_inicial"));
            atividade.setDataFinal(rs.getTimestamp("data_Final"));

            //Tipo Imóvel
            TipoImoveis tipoImoveis = new TipoImoveis();
            tipoImoveis.setId(rs.getLong("tipo_imovel_id"));
            tipoImoveis.setSigla(rs.getString("tipo_imovel_sigla"));
            tipoImoveis.setDescricao(rs.getString("tipo_imovel_descricao"));

            //boletim diario
            TratamentoAntiVetorial boletimDiario = new TratamentoAntiVetorial();
            boletimDiario.setId(rs.getLong("tratamento_antivetorial_fk"));
            atividade.setBoletimDiario(boletimDiario);

            atividade.setTipoImoveis(tipoImoveis);

            //Atividade inseticida
            List<AtividadeInseticida> atividadeInseticidaList = new ArrayList<AtividadeInseticida>();
            atividadeInseticidaList = getInseticida(id, conn);
            atividade.setAtividadeInseticidasList(atividadeInseticidaList);

            //AtividadeCriadouro
            List<AtividadeCriadouro> atividadeCriadouroList = new ArrayList<>();
            atividadeCriadouroList = getCriadouro(id, conn);
            atividade.setAtividadeCriadouroList(atividadeCriadouroList);

        }
        rs.close();
        ps.close();
        return atividade;
    }

    @Override
    public List<Atividade> readByCriteria(Map<String, Object> criteria, Connection conn) throws Exception {
        List<Atividade> atividadeList = new ArrayList<Atividade>();
        Atividade atividade = null;
        TipoImoveis tipoImoveis = null;
        String sql = "SELECT atividade .*,tipo_imovel.id as tipo_imovel_id, tipo_imovel.descricao as tipo_imovel_descricao, tipo_imovel.sigla as tipo_imovel_sigla,";
        sql += " quarteirao.id as quarteirao_id, quarteirao.descricao as quarteirao_descricao, bairro.nome as bairo_nome ";
        sql += "FROM atividade LEFT JOIN tipo_imovel on tipo_imovel.id = atividade.tipo_imovel_fk  ";
        sql += "LEFT JOIN quarteirao on quarteirao.id = atividade.quarteirao_fk ";
        sql += "LEFT JOIN bairro on bairro.id = quarteirao.bairro_fk ";
        sql += "WHERE 1 = 1";

        Statement s = conn.createStatement();

        Long criterionBoletimIdEq = (Long) criteria.get(CRITERION_BOLETIM_ID_EQ);
        if (criterionBoletimIdEq != null && criterionBoletimIdEq > 0) {

            sql += " AND tratamento_antivetorial_fk ='" + criterionBoletimIdEq + "'";
        }

        Long criterionBairroIdEq = (Long) criteria.get(CRITERION_BAIRRO_ID_EQ);
        if (criterionBairroIdEq != null && criterionBairroIdEq > 0) {
            sql += " AND bairro.id = '" + criterionBairroIdEq + "' ";
        }

        String criterioLatLngOk = (String) criteria.get(CRITERION_LAT_LNG_OK);
        if (criterioLatLngOk == "1") {
            sql += " AND latitude != '' and longitude != '' ";
        }

        java.util.Date criterionDataInicialEq = (java.util.Date) criteria.get(CRITERION_DATA_INICIAL_EQ);
        if (criterionDataInicialEq != null) {
            sql += " AND data_inicial >= '" + criterionDataInicialEq.toString() + "'";
        }

        java.util.Date criterionDataFinalEq = (java.util.Date) criteria.get(CRITERION_DATA_FINAL_EQ);
        if (criterionDataFinalEq != null) {
            sql += " AND data_final <= '" + criterionDataFinalEq.toString() + "'";
        }

        ResultSet rs = s.executeQuery(sql);

        while (rs.next()) {
            atividade = new Atividade();
            atividade.setId(rs.getLong("id"));
            atividade.setEndereco(rs.getString("endereco"));
            Quarteirao quarteirao = new Quarteirao();
            quarteirao.setId(rs.getLong("quarteirao_id"));
            quarteirao.setDescricao(rs.getString("quarteirao_descricao"));
            atividade.setQuarteirao(quarteirao);
            atividade.setNumero(rs.getString("numero"));
            atividade.setObservacao(rs.getString("observacao"));
            atividade.setInspecionado(rs.getInt("inspecionado"));
            atividade.setLatitude(rs.getString("latitude"));
            atividade.setLongitude(rs.getString("longitude"));
            atividade.setDataInicial(rs.getDate("data_inicial"));
            atividade.setDataFinal(rs.getDate("data_Final"));

            //Tipo Imóvel
            tipoImoveis = new TipoImoveis();
            tipoImoveis.setId(rs.getLong("tipo_imovel_id"));
            tipoImoveis.setSigla(rs.getString("tipo_imovel_sigla"));
            tipoImoveis.setDescricao(rs.getString("tipo_imovel_descricao"));

            atividade.setTipoImoveis(tipoImoveis);

            //Adicionando na Lista
            atividadeList.add(atividade);

        }

        return atividadeList;

    }

    @Override
    public void update(Atividade entity, Connection conn) throws Exception {

        String sql = "UPDATE atividade SET endereco=?, numero=?, observacao=?, inspecionado=?, tipo_imovel_fk=?, tratamento_antivetorial_fk=?, quarteirao_fk=?, latitude=?, longitude=?, data_inicial =?, data_final=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        int i = 0;
        ps.setString(++i, entity.getEndereco());
        ps.setString(++i, entity.getNumero());
        ps.setString(++i, entity.getObservacao());
        ps.setInt(++i, entity.getInspecionado());
        ps.setLong(++i, entity.getTipoImoveis().getId());
        ps.setLong(++i, entity.getBoletimDiario().getId());
        ps.setLong(++i, entity.getQuarteirao().getId());
        ps.setString(++i, entity.getLatitude());
        ps.setString(++i, entity.getLongitude());
        ps.setTimestamp(++i, new Timestamp(entity.getDataInicial().getTime()));
        ps.setTimestamp(++i, new Timestamp(entity.getDataFinal().getTime()));
        ps.setLong(++i, entity.getId());
        ps.execute();
        ps.close();

        //deletendo criadouros
        String sqldelCriadouro = "DELETE FROM atividade_criadouro WHERE atividade_fk = ?";
        PreparedStatement psdelCriadouro = conn.prepareStatement(sqldelCriadouro);
        psdelCriadouro.setLong(1, entity.getId());
        psdelCriadouro.execute();
        psdelCriadouro.close();

        //inserindo criadouro
        List<AtividadeCriadouro> atividadeCriadouroList = entity.getAtividadeCriadouroList();
        if (atividadeCriadouroList != null && atividadeCriadouroList.size() > 0) {
            String sqlCriadouro = "INSERT INTO atividade_criadouro(atividade_fk, criadouro_fk, quantidade)VALUES (?, ?, ?);";
            PreparedStatement psCriadouro = conn.prepareStatement(sqlCriadouro);
            for (AtividadeCriadouro atividadeCriadouro : atividadeCriadouroList) {
                psCriadouro.setLong(1, entity.getId());
                psCriadouro.setLong(2, atividadeCriadouro.getCriadouro().getId());
                psCriadouro.setInt(3, atividadeCriadouro.getQuantidadeCriadouro());
                psCriadouro.execute();
            }
            psCriadouro.close();
        }

        //deletando inseticidas 
        String sqldelInseticida = "DELETE FROM atividade_inseticida WHERE atividade_fk = ?";
        PreparedStatement psdelInseticida = conn.prepareStatement(sqldelInseticida);
        psdelInseticida.setLong(1, entity.getId());
        psdelInseticida.execute();
        psdelInseticida.close();

        //inserindo Inseticida
        List<AtividadeInseticida> atividadeInseticidaList = entity.getAtividadeInseticidasList();
        if (atividadeInseticidaList != null && atividadeInseticidaList.size() > 0) {
            String sqlInseticida = "INSERT INTO atividade_inseticida(atividade_fk, inseticida_fk, quantidade)VALUES (?, ?, ?);";
            PreparedStatement psinseticida = conn.prepareStatement(sqlInseticida);
            for (AtividadeInseticida atividadeInseticida : atividadeInseticidaList) {
                psinseticida.setLong(1, entity.getId());
                psinseticida.setLong(2, atividadeInseticida.getInseticida().getId());
                psinseticida.setInt(3, atividadeInseticida.getQuantidadeInseticida());
                psinseticida.execute();
            }
            psinseticida.close();
        }

    }

    @Override
    public void delete(Long id, Connection conn) throws Exception {

        //deletendo criadouros
        String sqldelCriadouro = "DELETE FROM atividade_criadouro WHERE atividade_fk = ?";
        PreparedStatement psdelCriadouro = conn.prepareStatement(sqldelCriadouro);
        psdelCriadouro.setLong(1, id);
        psdelCriadouro.execute();
        psdelCriadouro.close();

        //deletando inseticidas 
        String sqldelInseticida = "DELETE FROM atividade_inseticida WHERE atividade_fk = ?";
        PreparedStatement psdelInseticida = conn.prepareStatement(sqldelInseticida);
        psdelInseticida.setLong(1, id);
        psdelInseticida.execute();
        psdelInseticida.close();

        String sql = "DELETE FROM atividade WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.execute();
        ps.close();

    }

    public List<AtividadeInseticida> getInseticida(Long id, Connection conn) throws Exception {
        Inseticida inseticida = null;
        AtividadeInseticida atividadeInseticida = new AtividadeInseticida();
        List<AtividadeInseticida> atividadeInseticidaList = new ArrayList<AtividadeInseticida>();
        String sql = "SELECT atividade_inseticida.*, atividade_inseticida.quantidade as atividade_inseticida_qtde, inseticida.id as inseticida_id, inseticida.nome as inseticida_nome FROM atividade_inseticida LEFT JOIN inseticida on inseticida.id = atividade_inseticida.inseticida_fk LEFT JOIN atividade on atividade.id = atividade_inseticida.atividade_fk WHERE atividade.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            inseticida = new Inseticida();
            inseticida.setId(rs.getLong("inseticida_id"));
            inseticida.setNome(rs.getString("inseticida_nome"));

            atividadeInseticida = new AtividadeInseticida();
            atividadeInseticida.setQuantidadeInseticida(rs.getInt("atividade_inseticida_qtde"));
            atividadeInseticida.setInseticida(inseticida);

            atividadeInseticidaList.add(atividadeInseticida);
        }
        rs.close();
        ps.close();
        return atividadeInseticidaList;
    }

    public List<AtividadeCriadouro> getCriadouro(Long id, Connection conn) throws Exception {
        List<AtividadeCriadouro> atividadeCriadouroList = new ArrayList<>();
        Criadouro criadouro = null;
        String sql = "SELECT atividade_criadouro.*, atividade_criadouro.quantidade as atividade_criadouro_qtde, criadouro.id as criadouro_id, ";
        sql += " criadouro.grupo as criadouro_grupo,  criadouro.recipiente as criadouro_recipiente";
        sql += " FROM atividade_criadouro ";
        sql += " LEFT JOIN criadouro on criadouro.id = atividade_criadouro.criadouro_fk ";
        sql += " LEFT JOIN atividade on atividade.id = atividade_criadouro.atividade_fk ";
        sql += " WHERE atividade.id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            criadouro = new Criadouro();
            criadouro.setId(rs.getLong("criadouro_id"));
            criadouro.setGrupo(rs.getString("criadouro_grupo"));
            criadouro.setRecipiente(rs.getString("criadouro_recipiente"));

            AtividadeCriadouro atividadeCriadouro = new AtividadeCriadouro();
            atividadeCriadouro.setQuantidadeCriadouro(rs.getInt("atividade_criadouro_qtde"));
            atividadeCriadouro.setCriadouro(criadouro);

            atividadeCriadouroList.add(atividadeCriadouro);
        }
        rs.close();
        ps.close();
        return atividadeCriadouroList;
    }

}
