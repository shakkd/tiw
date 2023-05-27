async function load(url, body, element) {
    await fetch(url + "?" + body)
    .then(res => {
        return res.text(); 
    })
    .then(html => {
		// Initialize the DOM parser
        var parser = new DOMParser();

        // Parse the text
        var doc = parser.parseFromString(html, "text/html");

        // You can now even select part of that html as you would in the regular DOM 
        // Example:
        // var docArticle = doc.querySelector('article').innerHTML;
		
		element.replaceWith(doc.body);
		
	})
	.catch(err => {
		console.log(err);
	});
	
	await fetch("js/" + url + ".js")
    .then(response => response.text())
    .then(txt => {
        var script = document.createElement("script");
        script.innerHTML = txt;
        document.body.appendChild(script);
    });
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}