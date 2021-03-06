$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
jQuery(document).ready(function () {
    // binds form submission and fields to the validation engine
    jQuery("#form").validationEngine('attach', {promptPosition: "bottomLeft", autoPositionUpdate: true});
});
function getFormData() {
    if (!$("#form").validationEngine('validate')) {
        return;
    }
    var id = $("input[name=id").val();
    var url = "";
    var domain = "";
    if (document.domain == "localhost") {
        domain = "/Spiaa"
    }
    if (id != null && id != "") {
        url = domain + "/bairro/alterar";
    } else {
        url = domain + "/bairro/novo";
        id = null;
    }
    var nome = $("input[name=nome").val();
    var coordenadas = document.getElementById("coordenadas").value;

    var jsonData = {
        id: id,
        nome: nome,
        coordenadas: coordenadas
    };
    $.ajax({
        url: url,
        data: JSON.stringify(jsonData),
        jsonData: jsonData,
        method: 'POST',
        dataType: 'text',
        contentType: 'application/x-www-form-urlencoded; charset=iso-8859-1',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/text");
            xhr.setRequestHeader("Content-Type", "text/plain");
            xhr.overrideMimeType('text/html;charset=iso-8859-1');
        }
    }).done(function (retorno) {
        if (retorno != null) {
            if (retorno == "success") {
                $('#successCreate').modal('show');
            }
        }
        setTimeout(function () {
            document.location.assign(domain + '/bairro');
        }, 3000);
    }).fail(function () {

    });
}

