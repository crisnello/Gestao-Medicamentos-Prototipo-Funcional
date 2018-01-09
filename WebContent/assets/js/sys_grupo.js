$(document).ready(function() {
	selecionarAba('aba_grupos');
 	initialize();
 	td_hover($("#hor-minimalist-a td"));
 });

/**
 * abrir dialog de excluir grupo
 */
function showDialogExcluir(){
	$('#dialog_grupo').dialog('open');
}

function iniciarDialogs(){
	
	$('#dialog_grupo').dialog({
		modal : true,
		width : 270,
		autoOpen : false,
		show: efeitoShow,
		hide: efeitoHide,
		buttons : {
			"Ok" : function() {
				$('#bt_excluir').click();
			},
			"Cancelar": function(){
				$(this).dialog("close");
				$("#grupo_adicionar").val("");
			}
		}
	});
	
}

function initialize(){
	iniciarDialogs();
	trataCampos();
}


function trataCampos(){
	  iniciarBotoes();
}