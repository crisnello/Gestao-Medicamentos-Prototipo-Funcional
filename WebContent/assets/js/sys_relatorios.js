function initialize(){
	var myDate=new Date();
	var dataConsulta = (myDate.getMonth()+1)+"/"+myDate.getFullYear();
	buscar_relatorios(dataConsulta);
}

function buscar_relatorios(date){
	$("#manutencao").html(htmlLoading);
	$.post(baseUrl+"relatorio/calculo_relatorio/"+date , function(data) {
		$('#manutencao').html(data);
	});
}

function iniciar_relatorios(){
	td_hover($("#div_datas td"));
	relatorio_quilometragem();
}

function relatorio_quilometragem(){
	var values = $("#km_values").val();
	if(!values){
		return;
	}
	var values = $.evalJSON($("#km_values").val());
	
	
	var data = new google.visualization.DataTable();
    data.addColumn('string', 'Objeto');
    data.addColumn('number', 'Total');
    data.addColumn('number', '+80km/h');
	
    var arrayData = [];
	for(var i=0;i<values.length;i++){
		var val = values[i];
		var array = [];
		array.push(val.placa +" - "+ val.modelo);
		if(val.km == null){
			array.push(0);
		}else{
			array.push(eval(eval(val.km).toFixed(2)));
		}
		if(val.km_over == null){
			array.push(0);
		}else{
			array.push(eval(eval(val.km_over).toFixed(2)));
		}
		arrayData.push(array);
	}
	
    data.addRows(arrayData);

    var barsVisualization = new google.visualization.ColumnChart(document.getElementById('quilometragem'));
    barsVisualization.draw(data, null);
}