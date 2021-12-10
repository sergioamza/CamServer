
let list = [];

let button = document.getElementById("diff");
button.checked = false;

fetch(`./capture/list`)
	.then(x => x.text())
	.then(data => {
		list = JSON.parse(data);
		for (let i in list) {
			let container = document.createElement("div");
			container.className = "img-container";
			let imgInfo = document.createElement("div");
			imgInfo.id = `img${list[i]}info`
			imgInfo.className = "info";
			container.appendChild(imgInfo);
			let img = document.createElement("img");
			img.id = `img${list[i]}`;
			img.className = 'img';
			container.appendChild((img));
			let imgdiff = document.createElement("img");
			imgdiff.id = `img${list[i]}diff`;
			imgdiff.className = 'img';
			container.appendChild(imgdiff);
			document.body.appendChild(container);
		}
		for (let i in list) {
			update(list[i]);
			updateInfo(list[i]);
			updatediff(list[i]);
		}
	});

function update(i) {
	fetch(`./capture/${i}`)
		.then(x => x.text())
		.then(data => {
			updateImgElement(`img${i}`, data);
			setTimeout(function () { update(i); }, 100);
		}).catch(r => {
			setTimeout(function () { update(i); }, 10000);
		});
}

function updatediff(i) {
	if (!button.checked) {
		setTimeout(function () { updatediff(i); }, 1000);
		return;
	}
	fetch(`./capture/${i}/diff`)
		.then(x => x.text())
		.then(data => {
			updateImgElement(`img${i}diff`, data);
			setTimeout(function () { updatediff(i); }, 100);
		}).catch(r => {
			setTimeout(function () { updatediff(i); }, 10000);
		});
}

function updateInfo(i)	{	
	fetch(`./capture/${i}/info`)
		.then(x => x.text())
		.then(data => {
			o = JSON.parse(data);
			let imgInfo = document.getElementById(`img${i}info`);
			Object.entries(o).forEach((entry) => {
				const [key, value] = entry;
				let property = document.getElementById(`img${i}${key}`);
				if(property === null)	{
					property = document.createElement('div');
					property.id =`img${i}${key}`;
					property.className = 'property';
					property.height = 20;
					property.innerHTML = `<input type="text"><div class="tag">${key}</div><div class="value">${value}</div>`;
					property.addEventListener('change', function (evt) {
						console.log(evt);    
						setProperty(i,key,evt.srcElement.value);
					});
					imgInfo.append(property)
				} else	{
					removeElementsByClass('value',property)
					let propValue = document.createElement('div');
					propValue.className = 'value'
					propValue.innerHTML = value;
					//property.getElementsByClassName("value");
					property.appendChild(propValue);
				}
			})			
			setTimeout(function () { updateInfo(i); }, 1000);
		}).catch(r => {
			setTimeout(function () { updateInfo(i); }, 10000);
		});;
}

function setProperty(i, property, value) {	
	return fetch(`./capture/${i}/${property}/${value}`);
}

function updateImgElement(elementId, strData) {
	let base64 = JSON.parse(strData).image;
	if (base64 != null) {
		let buffer = Uint8Array.from(atob(base64), c => c.charCodeAt(0));
		let blob = new Blob([buffer], { type: "image/jpg" });
		let url = URL.createObjectURL(blob);
		document.getElementById(elementId).src = url;
	}
}

function removeElementsByClass(className, parent){
    const elements = parent.getElementsByClassName(className);
    while(elements.length > 0){
        elements[0].parentNode.removeChild(elements[0]);
    }
}