package spiaa.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import spiaa.model.base.BaseDAO;
import spiaa.model.entity.Bairro;
import spiaa.model.entity.Quarteirao;

public class BairroDAO implements BaseDAO<Bairro> {

    public static final String CRITERION_NOME_ILIKE = "1";

    @Override
    public void create(Bairro entity, Connection conn) throws Exception {
        String sql = "INSERT INTO bairro(nome, coordenadas) VALUES (?,?)RETURNING id;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getCoordenadas());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            entity.setId(rs.getLong("id"));
        }
        rs.close();
        ps.close();

    }

    @Override
    public Bairro readById(Long id, Connection conn) throws Exception {
        Bairro bairro = null;
        String sql = "SELECT * FROM bairro WHERE id =?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            bairro = new Bairro();
            bairro.setId(rs.getLong("id"));
            bairro.setNome(rs.getString("nome"));
            bairro.setCoordenadas(rs.getString("coordenadas"));
        }
        rs.close();
        ps.close();
        return bairro;

    }

    @Override
    public List<Bairro> readByCriteria(Map<String, Object> criteria, Connection conn) throws Exception {
        List<Bairro> bairroList = new ArrayList<Bairro>();
        Bairro bairro = null;

        String sql = "SELECT bairro.*,(select count(id) from quarteirao where quarteirao.bairro_fk = bairro.id) as quarteirao FROM bairro WHERE 1=1 ";

        String criterionNomeILike = (String) criteria.get(CRITERION_NOME_ILIKE);
        if (criterionNomeILike != null && (!criterionNomeILike.isEmpty())) {
            sql += " AND nome ILKE '%" + criterionNomeILike + "%'";
        }

        sql += " order By bairro.nome ASC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            bairro = new Bairro();
            bairro.setId(rs.getLong("id"));
            bairro.setNome(rs.getString("nome"));
            bairro.setCoordenadas(rs.getString("coordenadas"));
            
            //verofica a quantidade de quarteirao
            Quarteirao quarteirao = new Quarteirao();
            quarteirao.setId(rs.getLong("quarteirao"));
            List<Quarteirao> quarteiraoList = new ArrayList<>();
            quarteiraoList.add(quarteirao);
            bairro.setQuarteiraoList(quarteiraoList);

            bairroList.add(bairro);
        }
        rs.close();
        ps.close();
        return bairroList;
    }

    @Override
    public void update(Bairro entity, Connection conn) throws Exception {
        String sql = "UPDATE bairro SET nome=?, coordenadas=? WHERE id=? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getCoordenadas());
        ps.setLong(3, entity.getId());
        ps.execute();
        ps.close();
    }

    @Override
    public void delete(Long id, Connection conn) throws Exception {
        String sql = "DELETE FROM bairro WHERE id=?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ps.execute();
        ps.close();
    }

}
