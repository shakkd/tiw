var rejbtn = document.getElementById("rejbtn")
rejbtn.addEventListener("click", reject)

var backbtn = document.getElementById("backbtn")
if (backbtn != null) backbtn.addEventListener("click", () => window.location.href = "home");

async function reject() {
	
	await fetch("esito", {
		method : 'POST'
	});
	
	document.getElementById("errbox").innerHTML = "il voto è stato rifiutato";
	rejbtn.disabled = true;
	
	document.getElementById("stato").value = "Rifiutato";
	
}
