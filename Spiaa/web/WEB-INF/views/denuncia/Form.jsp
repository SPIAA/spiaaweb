<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Bootstrap 3, from LayoutIt!</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <!--link rel="stylesheet/less" href="less/bootstrap.less" type="text/css" /-->
        <!--link rel="stylesheet/less" href="less/responsive.less" type="text/css" /-->
        <!--script src="js/less-1.3.3.min.js"></script-->
        <!--append ‘#!watch’ to the browser URL, then refresh the page. -->

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="js/html5shiv.js"></script>
        <![endif]-->

        <!-- Fav and touch icons -->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="img/apple-touch-icon-57-precomposed.png">
        <link rel="shortcut icon" href="img/icone_spiaa.png">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/scripts.js"></script>
    </head>

    <body>
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="row clearfix">
                        <div class="col-md-12 column">
                            <div class="col-md-2 column">
                                <img alt="" src="img/MiniLogo.png" class="img-responsive"> 
                            </div>
                            <div class="col-md-10 column">
                                <h3><b> Todos contra a Dengue!</b></h3>
                            </div>
                        </div>
                    </div>
                    <nav class="navbar navbar-default" role="navigation">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> 
                                <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span>
                                <span class="icon-bar"></span></button> <a class="navbar-brand" href="<c:url value="/home"/>" >Início</a>
                        </div>

                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul class="nav navbar-nav">
                                <li class="">
                                    <a href="<c:url value="/mapa"/>">Mapa da cidade</a>
                                </li>
                                <li>
                                    <a href="<c:url value="/denunciaform"/>">Denuncie</a>
                                </li>

                            </ul>

                            <ul class="nav navbar-nav navbar-right">
                                <li>
                                    <a href="<c:url value="/login"/>">Acesso restrito</a>
                                </li>

                            </ul>
                        </div>

                    </nav>
                </div>
            </div>

            <div class="row clearfix">

                <div class="col-md-3 column"></div>
                <div class="col-md-6 column" style="border: 1px silver solid; border-radius: 15px;">
                    <div class="col-md-12 text-center">
                        <h3>Relatório de denúncia</h3></div>
                    <form role="form" method="POST">

                        <div class="form-group">
                            <label for="exampleInputPassword1">Bairro:</label>
                            <select  class="form-control" name="bairro.id" id="estrato.id">
                                <c:forEach var="bairroList" items="${bairroList}">
                                    <option value="${bairroList.id}" > ${bairroList.nome}  </option>
                                </c:forEach>
                            </select>

                        </div>

                        <div class="form-group">
                            <label for="Inputendereco">Endereço:</label><input type="text" class="form-control" name="endereco" />
                        </div>

                        <div class="form-group">
                            <label for="Inputnumero">Numero:</label><input type="text" class="form-control" name="numero" />
                        </div>
                        <div class="form-group">
                            <label for="Inputtelefone">Telefone:</label><input type="text" class="form-control" name="telefone"/>

                        </div>
                        <div class="form-group">
                            <label for="Inputtipoirregularidade">Tipo de irregularidades:</label><textarea  class="form-control" name="irregularidade" ></textarea>
                        </div>
                        <div class="form-group">
                            <label for="Inputobservacao">Observação:</label><textarea  class="form-control" name="observacao" ></textarea>
                        </div>
                        <div class="form-group text-center">
                            <input class="btn btn-lg btn-success" type="submit" value=" Enviar Denuncia" /> 
                        </div>

                    </form>

                </div>

                <div class="col-md-3 column">

                </div>

            </div>
            <br/><br/>
            <hr>
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="col-md-12 column">
                        <div class="col-lg-3">
                            <img alt="" height="75" src="img/fai_mg.jpg">
                        </div>
                        <div class="col-lg-3">
                            <img alt="" height="75" src="img/selo_fai.jpg">
                        </div>
                        <div class="col-lg-3">
                            <img alt="" width="174" height="75" src="img/MiniLogo.png" class="img-responsive">
                        </div>
                        <div class="col-lg-3">
                            <img alt="" width="174" height="75" src="img/LogoFaitec2.png" class="img-responsive">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

