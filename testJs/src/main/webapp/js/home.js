btn = document.getElementById("refrbtn");
btn.addEventListener("click", refrFunc);

btn = document.getElementById("subbtn");
btn.addEventListener("click", postFunc);


async function refrFunc() {
		
	var form = document.getElementById("selform");

	var details = {
	    'corso': form.elements["corso"].value,
	    'mode': 'refresh',
	};
	
	var formBody = [];
	for (var property in details) {
	  var encodedKey = encodeURIComponent(property);
	  var encodedValue = encodeURIComponent(details[property]);
	  formBody.push(encodedKey + "=" + encodedValue);
	}
	formBody = formBody.join("&");
	
	
	var req = await fetch('home', {
	  method: 'POST',
	  headers: {
	    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
	  },
	  body: formBody
	});

	
	var dates = await req.json();
	var sel = document.getElementById("datesel");
		
	
	for(var i = sel.options.length - 1; i >= 0; i--)
		sel.remove(i);
	
	for(var i = 0; i < dates.length; i++) {
		var opt = document.createElement("option");
		opt.text = dates[i];
		opt.value = formatDate(dates[i]);
		sel.add(opt);
	} 
	
	
}

async function postFunc() {
	
	var form = document.getElementById("selform");

	var flag = sessionStorage.getItem("flag");

	var details = {
	    'data': form.elements["appello"].value,
		'corso': form.elements["corso"].value,
	};
	
	var formBody = [];
	for (var property in details) {
	  var encodedKey = encodeURIComponent(property);
	  var encodedValue = encodeURIComponent(details[property]);
	  formBody.push(encodedKey + "=" + encodedValue);
	}
	formBody = formBody.join("&");
	
	if (form.elements["appello"].value == "")
		document.getElementById("errbox").innerHTML = "select a course";
	else switch (flag) {
		case "D":
			formBody += "&order=Matricola";
			load("iscritti", formBody, document.body);
			break;
		case "S":
			formBody += "&idUtente=" + sessionStorage.getItem("idUtente");
			load("esito", formBody , document.body);		
			break;
	}
	
	
}
