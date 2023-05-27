var btnArray = document.getElementsByName("orderbtn");

btnArray.forEach(elem => {
    elem.addEventListener("click", () => sortTable(elem.value));
});

var invbtn = document.getElementById("invbtn")
invbtn.addEventListener("click", invia);

var invbtn = document.getElementById("pubblbtn")
invbtn.addEventListener("click", pubbl);

var invbtn = document.getElementById("verbalbtn")
invbtn.addEventListener("click", verbal);

var backbtn = document.getElementById("backbtn")
backbtn.addEventListener("click", () => window.location.href = "home");


function sortTable(n) {
	
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("studTable");
  switching = true;
  
    
  // Set the sorting direction to ascending:
  dir = "asc"; 
  
  /* Make a loop that will continue until
  no switching has been done: */
  while (switching) {
	  
    // Start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    
    /* Loop through all table rows (except the
    first, which contains table headers): */
    for (i = 1; i < (rows.length - 1); i++) {
		
      // Start by saying there should be no switching:
      shouldSwitch = false;
      
      /* Get the two elements you want to compare,
      one from current row and one from the next: */
      x = rows[i].getElementsByTagName("td")[n];
      y = rows[i + 1].getElementsByTagName("td")[n];
      
      /* Check if the two rows should switch place,
      based on the direction, asc or desc: */
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
			
          // If so, mark as a switch and break the loop:
          shouldSwitch = true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
			
          // If so, mark as a switch and break the loop:
          shouldSwitch = true;
          break;
        }
      }
    }
    
    if (shouldSwitch) {
      /* If a switch has been marked, make the switch
      and mark that a switch has been done: */
      
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      
      // Each time a switch is done, increase this count by 1:
      switchcount ++; 
    } else {
		
      /* If no switching has been done AND the direction is "asc",
      set the direction to "desc" and run the while loop again. */
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
      
    }
  }
}


async function invia() {
	
	var rowlist = document.getElementsByClassName("studrow");
	var invdata = [];
		
	var i = 0;
	for(var j = 0; j < rowlist.length; j++) if (rowlist[j].children[5].value != "null") {
		invdata[i] = {};
		invdata[i]["matr"] = rowlist[j].children[0].innerHTML;
		invdata[i]["esito"] = rowlist[j].children[5].value;
		rowlist[j].children[5].disabled = true;
		
		i++;
	}
	
	console.log(invdata);
	
	await fetch("stud", {
	  method: 'POST',
	  headers: {
	    'Content-Type': 'application/json'
	  },
	  body: JSON.stringify(invdata)
	});



	
	
	
}


async function pubbl() {

	var details = {
	    'submit': 'pubblica',
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
	
	
	var arr = document.getElementsByClassName("tdstato");
	
	for (var tmp in arr)
		if (tmp.innerHTML = "Inserito") tmp.innerHTML = "Pubblicato";
	
	
}

async function verbal() {
	
	var details = {
	    'submit': 'verbalizza',
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
	
	var arr = document.getElementsByClassName("tdstato");
	
	for (var tmp in arr)
		if (tmp.innerHTML = "Pubblicato") tmp.innerHTML = "Verbalizzato";
	
}