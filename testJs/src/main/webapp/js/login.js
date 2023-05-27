
btn = document.getElementById("subbtn");
btn.addEventListener("click", postFunc);

fetch("js/utils.js")
    .then(response => response.text())
    .then(txt => {
        var script = document.createElement("script");
        script.innerHTML = txt;
        document.body.appendChild(script);
});

async function postFunc() {
	
	var form = document.getElementById("loginform");

	var details = {
	    'email': form.elements["email"].value,
	    'pass': form.elements["pass"].value,
	};
	
	var formBody = [];
	for (var property in details) {
	  var encodedKey = encodeURIComponent(property);
	  var encodedValue = encodeURIComponent(details[property]);
	  formBody.push(encodedKey + "=" + encodedValue);
	}
	formBody = formBody.join("&");
	
	
	var req = await fetch('login', {
	  method: 'POST',
	  headers: {
	    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
	  },
	  body: formBody
	});

	
	var json = await req.json();
	
	
	console.log(json);
	
	if (json[0] == "D" || json[0] == "S") {
		
		sessionStorage.setItem("flag", json[0]);
		sessionStorage.setItem("idUtente", json[1]);
		
		//load("home", document.body);
		window.location.href = "home";
	} else 
		document.getElementById("errbox").innerHTML = "access denied";
	
	
}

