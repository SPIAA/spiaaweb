<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../imports.jspf" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <jsp:include page="../template-admin/header.jsp"/>
    </head>
    <body>
        <div id="wrapper"  class="col-lg-12">
            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
                <jsp:include page="../template-admin/menutop.jsp"/>
            </nav>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Tratamento Anti-Vetorial
                                <small>Listagem</small>
                            </h1>
                            <br/>                              
                            <div class="form-inline">
                                <div class="form-group col-md-3">
                                    <label>Data Boletim </label><br/>
                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${boletimDiario.dataBoletim}" />
                                </div>
                                <div class="form-group col-md-3">
                                    <label class="">Agente : </label>
                                    <br/>${boletimDiario.usuario.nome}
                                </div>
                                <div class="form-group col-md-3">
                                    <label class="">Bairro : </label>
                                    <br/>${boletimDiario.bairro.nome}
                                </div>
                                <div class="form-group col-md-3">
                                    <label class="">Agente : </label>
                                    <br/>${boletimDiario.turma}
                                </div> 
                                <div class="form-group col-md-3">
                                    <label class="">Número : </label>
                                    <br/>${boletimDiario.numero}
                                </div> 
                                <div class="form-group col-md-3">
                                    <label class="">Semana : </label>
                                    <br/>${boletimDiario.semana}
                                </div> 
                                <div class="form-group col-md-3">
                                    <label class="">Nº Atividade : </label>
                                    <br/>${boletimDiario.numeroAtividade}
                                </div> 
                                <div class="form-group col-md-3">
                                    <label class="">T. Atividade : </label>
                                    <br/>${boletimDiario.tipoAtividade}
                                </div> 
                            </div>

                            <br/>  <br/>  <br/><br/>  <br/>  <br/><br/>  

                            <a href="#"  name="cancelar" class="btn btn-default" onclick="javascript:history.back();" value="cancelar">Cancelar</a>
                            <a class="btn btn-default" href="<c:url value="/atividade/${boletimDiario.id}/novo"/>"><i class="fa fa-fw fa-plus"></i>  &nbsp;&nbsp;Novo &nbsp;&nbsp;</a>
                            <br/>

                            <br/><br/>
                            <table id="table_id" class="table table-striped table-bordered" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th>Quarteirão</th>
                                        <th>Endereço</th>
                                        <th>Número</th>
                                        <th>Observação</th>
                                        <th>T. Inspec.</th>
                                        <th align="center" style="width:70px;" class="no-sort"> </th>
                                        <th align="center" style="width:70px;" class="no-sort"> </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${atividadeList}" var="atividade">
                                        <tr>
                                            <td>${atividade.quarteirao.descricao}</td>
                                            <td>${atividade.endereco}</td>
                                            <td>${atividade.numero}</td>
                                            <td>${atividade.observacao}</td>
                                            <td>${atividade.inspecionado}</td>
                                            <td align="center"><a href="../atividade/${atividade.id}/alterar" data-toggle="tooltip" data-placement="top" title="Alterar"><i class="fa fa-2x fa-edit text-primary"></i></a></td>
                                            <td align="center"><a href="../atividade/${atividade.id}/excluir" class="btn_pag " data-toggle="tooltip" data-placement="top" title="Excluir"><i class="fa fa-2x fa-trash-o text-danger"></i></a></td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                        <!-- /.row -->
                    </div>
                    <!-- /.container-fluid -->
                </div>
                <!-- /#page-wrapper -->
            </div>
            <!-- /#wrapper -->
            <div class="modal fade" id="confirmDelete">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header btn-danger">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h3 class="modal-title">Atenção</h3>
                        </div>
                        <div class="modal-body">
                            <p>Você deseja realmente excluir?</p>
                            <div class="alert alert-danger">
                                Atenção: esta operação não pode ser desfeita.
                            </div>
                        </div>
                        <div class="modal-footer">
                            <a href="#" class="btn btn-danger">Sim, desejo excluir.</a>
                            <a href="#" class="btn btn-default" data-dismiss="modal">Cancelar</a>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            <script>
                $('.btn_pag').on("click", function () {
                    $('#confirmDelete').modal('show');
                    $("#confirmDelete .btn-danger").attr("href", $(this).attr("href"));
                    return false;
                });
            </script>
    </body>
</html>