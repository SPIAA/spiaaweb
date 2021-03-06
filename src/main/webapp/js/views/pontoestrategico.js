
jQuery(document).ready(function () {
    // binds form submission and fields to the validation engine
    jQuery("#form").validationEngine('attach', {promptPosition: "bottomLeft", autoPositionUpdate: true});
});
function getFormData() {
    if (!$("#form").validationEngine('validate')) {
        return;
    }
    var id = document.getElementById("id").value;
    var url = ""
    var domain = "";
    if (document.domain == "localhost") {
        domain = "/Spiaa"
    }

    if (id != null && id != "") {
        url = domain + "/pontoestrategico/alterar";
    } else {
        id = null;
        url = domain + "/pontoestrategico/novo";
    }
    var rua = document.getElementById("rua").value;
    var numero = document.getElementById("numero").value;
    var bairro = document.getElementById("bairro.id").value;
    var complemento = document.getElementById("complemento").value;
    var cep = document.getElementById("cep").value;
    var cidade = document.getElementById("cidade").value;
    var uf = document.getElementById("uf").value;
    var ramoAtividade = document.getElementById("ramoAtividade").value;
    var usuario = document.getElementById("usuario.id").value;
    var latitude = document.getElementById("latitude").value != null ? document.getElementById("latitude").value : "";
    var longitude = document.getElementById("longitude").value;
    var jsonData = {
        id: id,
        rua: rua,
        numero: numero,
        complemento: complemento,
        bairro: {id: bairro},
        cep: cep,
        cidade: cidade,
        estado: uf,
        ramoAtividade: ramoAtividade,
        latitude: latitude,
        longitude: longitude,
        usuario: {id: usuario}
    };
    $.ajax({
        url: url,
        data: JSON.stringify(jsonData),
        jsonData: jsonData,
        method: 'POST',
        dataType: 'text',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/text");
            xhr.setRequestHeader("Content-Type", "text/plain");
            xhr.overrideMimeType('text/html;charset=iso-8859-1');
        }
    }).done(function (retorno) {
        if (retorno != null) {
            $('#successCreate').modal('show');
        }
        setTimeout(function () {
            document.location.assign(domain + '/pontoestrategico');
        }, 3000);
    }).fail(function () {

    });
}
