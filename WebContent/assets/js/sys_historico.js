var dir_icons = "";
var dir_arrows = "";
var posicionamentos;

$(document).ready(function() {
	selecionarAba('aba_historico');
	initialize();
});

function initialize() {
	init_botao_buscar_endereco();
	initialize_map(true);
	resize_map();
	dir_icons = baseUrl+"assets/images/numeric/";
	dir_arrows = baseUrl+"assets/images/arrow/";
}

function buscar() {
	
	var posicionamentos = $.evalJSON($("#posicionamentos").val());
	
	if (poly) {
		poly.setMap(null);
	}
	
	if(posicionamentos.length == 0){
		clearOverlays();
		return;
	}
	
	var polyOptions = {
		clickable:false,
		strokeColor : '#1874CD',
		strokeOpacity : 1.0,
		strokeWeight : 3
	};
	poly = new google.maps.Polyline(polyOptions);
	poly.setMap(map);
	path = poly.getPath();
	path = [];
	clearOverlays();
	
	//carrega as cercas do veiculo selecionado
	limparPolygons();
	carregar_cercas_veiculo($("#combo_veiculos_input").val());
	
	//trata os posicionamentos retornados
	for ( var total = 0; total < posicionamentos.length; total++) {
		var pos = posicionamentos[total];
		processarPosicionamento(pos,total);
	}

	var myLatLng = new google.maps.LatLng(
			posicionamentos[0].lat,
			posicionamentos[0].lon);
	map.setCenter(myLatLng);
	map.setZoom(15);

	window.location = "#map_canvas";
}

function processarPosicionamento(pos,atual,is_ultimo) {
	var ang = parseFloat(pos.angulo);
	
	var image = "";
	
	var subfolder = "";
	
	if(pos.velocidade < 80 && pos.velocidade > 60) {
		subfolder = "orange/";
	}else if (pos.velocidade > 80) {
		subfolder = "red/";
	}
	
	
	if (ang > 22 && ang <= 67) {
		image = dir_arrows + subfolder + "upper_right.png";
	} else if (ang > 67 && ang <= 112) {
		image = dir_arrows +  subfolder +"right.png";
	} else if (ang > 112 && ang <= 157) {
		image = dir_arrows +  subfolder +"lower_right.png";
	} else if (ang > 157 && ang <= 202) {
		image = dir_arrows +  subfolder +"down.png";
	} else if (ang > 202 && ang <= 247) {
		image = dir_arrows +  subfolder +"lower_left.png";
	} else if (ang > 247 && ang <= 292) {
		image = dir_arrows +  subfolder +"left.png";
	} else if (ang > 292 && ang <= 337) {
		image = dir_arrows +  subfolder +"upper_left.png";
	}else{
		image = dir_arrows +  subfolder +"up.png";
	}
	
	var icon = new google.maps.MarkerImage(image, new google.maps.Size(16, 16), new google.maps.Point(0,0), new google.maps.Point(8, 8));
	
	var myLatLng = new google.maps.LatLng(pos.lat, pos.lon);
	
	if (pos.parado) {
		icon = dir_icons + "blackP.png";
	}
	
	var marker = new google.maps.Marker({
		position : myLatLng,
		map : map,
		icon : icon
	});

	map_markers.push(marker);

	var path = poly.getPath();
	path.push(myLatLng);

	var html = "<div class='infowindow'>";
	if (pos.parado) {
		html += "<div class='header'>";
		html += "Parou";
		html += "</div>"; 
		html += pos.de;
		html += "<br>até<br>";
		html += pos.data_capturado;
		html += "</div>";
	}else{
		html += "<div class='header'>";
		html += pos.data_capturado;
		html += "</div>"; 
		html += "Velocidade: "	+ parseInt(pos.velocidade) + " Km/h";
	}
	
	var infowindow = new google.maps.InfoWindow({
		content : html
	});

	google.maps.event.addListener(marker, 'click', function() {
		deleteInfos();
		infowindow.open(map, marker);
		infosArray.push(infowindow);
	});
	
	if(is_ultimo){
		infowindow.open(map, marker);
		infosArray.push(infowindow);
	}
}