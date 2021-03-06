<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<%@include file="../imports.jspf" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <jsp:include page="../template-admin/header.jsp"/>
        <link href="<c:url value="/css/jquery-ui.min.css"/>" rel="stylesheet">
        <script src="<c:url value="/js/jquery-ui.min.js"/>"></script>
        <script src="<c:url value="/js/views/criadouro.js"/>"></script>
    </head>
    <body>
        <div id="wrapper">
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
                                Criadouro
                                <c:if test="${not empty criadouro}"><small>Alterar</small></c:if>
                                <c:if test="${empty criadouro}"><small>Novo</small></c:if>
                                </h1>
                            <c:if test="${not empty mensagem}">  <div class="alert alert-danger alert-dismissible" role="alert">
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <p class="text-center">${mensagem}</p> 
                                </div>
                            </c:if>
                            <form class="form-group" method="POST" id="form">
                                <input type="text" hidden="" id="id" name="id" value="${criadouro.id}"/>
                                <div class="form-group col-md-4 ">
                                    <label for="nome">Nome: </label>
                                    <input type="text" class="form-control form-control validate[required]" id="grupo" name="grupo" value="${criadouro.grupo}"/>
                                </div>
                                <div class="form-group col-md-4 ">
                                    <label for="unidade">Recipiente : </label>
                                    <input type="text" class="form-control form-control validate[required]" id="recipiente"  name="recipiente" value="${criadouro.recipiente}"/>
                                </div>
                                <div class="col-lg-4" align="center">
                                    <br/>
                                     <a href="#"  name="cancelar" class="btn btn-default" onclick="javascript:history.back();" value="cancelar"> &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp; Cancelar &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;</a>
                                        
                                    <input class="btn btn-success " onclick="getFormData()" value="Salvar" />  
                                </div>
                            </form>
                            <!--<button onclick="myFunction()">report via ajax</button>-->
                            <br/>
                        </div>
                    </div>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <div class="modal fade" id="successCreate">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header btn-success">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h3 class="modal-title">SPIAA</h3>
                    </div>
                    <div class="modal-body text-center">
                        <div class="alert alert-success">
                            <strong> Criadouro salvo com sucesso</strong> <br/>
                            <i class="fa fa-2x fa-spinner fa-pulse"></i>
                            <p>Voc� est� sendo redirecionado.....</p>
                        </div>

                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </body>
</html>
