
(function() {

	// Component that handles the Conferences Table
	var topCategoriesList = new TopCategoriesList(document.getElementById("catalogdiv")
		,document.getElementById("cataloglist"));
		
	var confirmButton = document.getElementById("confirmbutton");
	confirmButton.style.display = "none";
	
	var denyButton = document.getElementById("denybutton");
	denyButton.style.display = "none";
	
	var saveButton = document.getElementById("savebutton");
	saveButton.style.display = "none";

	// Component that handles the form to create a conference
	var createCategoryForm = new CreateCategoryForm(document.getElementById("createcategoryform"));
	var divForm = document.getElementById("divform");
	//Array di updates
	var updates = new Array();
	
	//var allCategoriesList = new AllCategoriesList(document.getElementById("listacategoria"))

	/* On load, the conferences list must be shown and 
	   register the event to specify what to do when the submit button of the form is clicked
	*/
	window.addEventListener("load", () => {
	  topCategoriesList.show();
	  createCategoryForm.registerClick();
	  }, false);


	/* Generic function create to handle any request 
		method = "GET" or "POST"
		url = URL to send the request to
		formElement = form to send in body (if apply, null otherwise)
		cback = callback to invoke when status change to handle responses from server
		reset = if we use formElement then if we reset it or not
	*/
	/*function makeCall(method, url, formElement, cback, reset = true) {
	    var req = new XMLHttpRequest(); 
	    req.onreadystatechange = function() {
			cback(req)
	  	};
	    
	    req.open(method, url);

	    if (formElement == null) {
	      req.send();
	    } else {
	      console.log("sending...")
	      req.send(new FormData(formElement));
	      if (reset) {
	      	formElement.reset();
	      }
	    }

	  }*/

	// Component that handles the Conferences Table
	function TopCategoriesList( _listcontainer,_listcontainerbody) {
		    
		    this.listcontainer = _listcontainer;
		    this.listcontainerbody = _listcontainerbody;

		    this.show = function() {
		      
		      var self = this; //Important!

		      makeCall("GET", "GoToHomePage", null,
		        // callback function
		        function(req) {
		          if (req.readyState == XMLHttpRequest.DONE) { // == 4
		            var message = req.responseText;
		            if (req.status == 200) {
		              var conferencesToShow = JSON.parse(req.responseText);

		              if (conferencesToShow.length == 0) {
		                alert("Nothing to show"); //for demo purposes
		                return;
		              }
						//console.log(conferencesToShow.length);
		              // If conferences list is not emtpy, then update view
		              self.update(conferencesToShow); // self visible by closure
		            }
		           else {
		           	// request failed, handle it
		           	self.listcontainer.style.visibility = "hidden";
		            alert("Not possible to recover data"); //for demo purposes
		          }
		      }
		        }
		      );
		    };


		    this.update = function(categoriesArray) {
		      var elem, i, row, destcell, datecell, linkcell, anchor;
		      this.listcontainerbody.innerHTML = ""; // empty the table body
		      
		      // build updated list
		      var self = this;
				var catalog = document.getElementById("cataloglist");
				
				var selectCategory = document.getElementById("listacategorie");
				
				var cattomove = null;
				//cattomovenow.nomecat = null;
				//cattomovenow.nomefathercat = null;
				//console.log(arrayCreatedByRecursion.length)
				//Qui dentro va nested iterazione se gson non mantiene albero
		      categoriesArray.forEach(function(conference) { // self visible here, not this
		        //Create a row for each conference
				var category = document.createElement("li")
				var cat = document.createElement("ol")
		        category.innerText = conference.nome_categoria;
				//category.value = conference.nome_categoria;
				category.draggable = true;
				category.ondrop = true;
				
				//Popola il form con le scelte
				var opzione = document.createElement("option");
				opzione.value = conference.nome_categoria;
				opzione.nome_nuovacategoria = conference.nome_categoria;
				opzione.innerText = conference.nome_categoria;
				//console.log(document.getElementById("listacategorie").value)
				//console.log(opzione.value)
				
				let startElement;
				
				category.addEventListener("dragstart", function(event) {
					//startList = event.target.closest("ol");
					startElement = event.target.closest("li");
					//upperElement = startElement.before("li")
				})
				category.addEventListener("dragover", function(event) {
					event.preventDefault();
					var dest = event.target.closest("li");
					dest.className = "selected";
				})
				category.addEventListener("dragleave", function(event) {
					var dest = event.target.closest("li");
					dest.className = "notselected";
				})
				category.addEventListener("drop", function(event) {
					event.preventDefault();
					var dest = event.target.closest("li");
					//dest.appendChild(startElement);
					confirmButton.style.display = "block";
					denyButton.style.display = "block";
					saveButton.style.display = "block";
					
					if (cattomove === null) {
						
						//cattomove = new Object();
						
						if (window.confirm("Are you sure?")) {
									cattomove = new Object();
									dest.appendChild(startElement);
									
									//cattomovenow = new Object();
								  	cattomove.nomecat = startElement.testo
									cattomove.nomefathercat = null;
									//if (cattomove === null)	cattomove = cattomovefunct
									updates.push(cattomove)
									divForm.style.display = "none";
									
									console.log(cattomove.nomecat)
									//console.log(cattomove.nomefathercat)
									
								} else {
									//Controlla caso padri assoluti
									//upperElement.appendChild(startElement)
									}
						
						//dest.appendChild(startElement);
						//cattomove.nomecat = conference.nome_categoria
						//cattomove.nomefathercat = null;
						//updates.push(cattomove)
						//divForm.style.display = "none";
					}
					
					
					
				})
				//This works!
				//catalog.appendChild(category);
				//category.appendChild(cat);
				
				catalog.appendChild(category);
				//category.appendChild(cat);
				//category.appendChild(cat)
				//cat.appendChild(category)
				
				selectCategory.appendChild(opzione);
				
				
				var nuovool = document.createElement("ol")
				category.appendChild(nuovool)
								
				function findSubCategories(conference, olList) {
					//var nuovool = document.createElement("ol")
					//category.appendChild(nuovool)
					conference.subCategories.forEach (sub => {
						var subCategory = document.createElement("li");
						//var subs = document.createElement("ol")
						
						//Form
						var opt = document.createElement("option");
						opt.value = sub.nome_categoria;
						opt.innerText = sub.nome_categoria;
						selectCategory.appendChild(opt);
						
						subCategory.testo = sub.nome_categoria;
						//console.log(sub.nome_categoria)
						subCategory.nodeValue = sub.nome_categoria
						subCategory.innerText = sub.nome_categoria;
						//subCategory.className = sub.nome_categoria
						//subCategory.nodeValue = sub.nome_categoria;
						//console.log(subCategory.nodeValue)
						subCategory.draggable = true;
						subCategory.ondrop = true;
						
						//L'assegnamento di event listener va spostato fuori da ricorsione forse 
						//Esatto siccome qua dentro sono dentro la funzione update!!
						
						//Aggiungere eventi di drag a subcategories?
						subCategory.addEventListener("dragstart", function(event) {
					//startList = event.target.closest("ol");
						startElement = event.target.closest("li");
						//upperElement = startElement.parentElement;
						//console.log(upperElement)
						})
						subCategory.addEventListener("dragover", function(event) {
							event.preventDefault();
							var dest = event.target.closest("li");
							//dest.className = "selected";
						})
						subCategory.addEventListener("dragleave", function(event) {
							var dest = event.target.closest("li");
							//dest.className = "notselected";
						})
						subCategory.addEventListener("drop", function(event) {
							event.preventDefault();
							var dest = event.target.closest("li");
							//dest.appendChild(startElement);
							//confirmButton.style.display = "block";
							//denyButton.style.display = "block";
							saveButton.style.display = "block";
							//Bisogna creare nuovo oggetto con sia nome categoria sia categoria in cui è spostato
							//Forse va in una nuova funzione
							
							/*if (arrayCreatedByRecursion === null) {
								var categoryMoved = new Object();
								categoryMoved.nomecat = conference.nome_categoria;
								console.log(categoryMoved.nomecat)
								//Padre assoluto ha padre null
								categoryMoved.nomefathercat = null;
								console.log(categoryMoved.nomefathercat)
								arrayCreatedByRecursion = categoryMoved

			
							}*/
							
							//var categoryMoved = new Object();
							//Sistema qui
							//BiSOGNA SPOSTARE FUNZIONE DROP FUORI SEENò VIENE CHIAMATA SEMPRE a causa della ricorsione
							//Fixato con startelement -> Adesso sistemare creazione di un nuovo oggetto bloccanddo ricorsione
							//categoryMoved.nomecat = startElement.testo;
							//console.log(categoryMoved.nomecat)
							//categoryMoved.nomefathercat = conference.nome_categoria;
							//console.log(categoryMoved.nomefathercat)
							//updates.push(categoryMoved)
							//console.log(updates)
							//TopCategoriesList.show();
							
							if (cattomove === null) {
								//cattomove = new Object();
								if (window.confirm("Are you sure?")) {
									cattomove = new Object();
									dest.appendChild(startElement);
									
									//cattomovenow = new Object();
								  	cattomove.nomecat = startElement.testo
									//Check conference
									cattomove.nomefathercat = conference.nome_categoria
									//if (cattomove === null)	cattomove = cattomovefunct
									updates.push(cattomove)
									divForm.style.display = "none";
									
									console.log(cattomove.nomecat)
									console.log(cattomove.nomefathercat)
									
								} else {
									//upperElement.appendChild(startElement)
									//Appendi category dove era prima
									
								}
								//cattomovenow.nomecat = startElement.testo
								//cattomovenow.nomefathercat = conference.nome_categoria
								//updates.push(cattomovenow)
								//divForm.style.display = "none";

								//createCategoryForm.style.display = "none";
							}		
							//console.log(cattomovefunct.nomecat)
							//console.log(cattomovefunct.nomefathercat)
						})
						
						//Fix here
						//category.appendChild(subCategory);
						olList.appendChild(subCategory)
						//nuovool.appendChild(subCategory);
						//subCategory.appendChild(subs);
						var newOl = document.createElement("ol");
						subCategory.appendChild(newOl);
						
						findSubCategories(sub, newOl);
						
						//cattomove = null
						
					})
				}
				
				//function cycleChildren(conference) {
					//var catList = conference;
					//conference.forEach ( sub => {
						
					//})
				//}
								
				findSubCategories(conference, nuovool)
				//updates.push(cattomovenow)
				console.log(updates)			

		      });
		      
				//cattomove = null;
				
			  /*resetArrows(this.listcontainer.querySelectorAll('th'));
		      this.listcontainer.style.visibility = "visible";*/
		    }
		  }

	

	function dragStart(event) {
		var startElement = event.target.closest("li")
	}
	
	function dragOver(event) {
		event.preventDefault();
		var dest = event.target.closest("li");
		dest.className = "selected"; 
	}
	
	function dragLeave(event) {
		var dest = event.target.closest("li");
		dest.className = "notselected";
	}
	
	function drop(event) {
		event.preventDefault();
		var dest = event.target.closest("li");
		dest.appendChild(startElement);
		confirmButton.style.display = "block";
		denyButton.style.display = "block";
		saveButton.style.display = "block";
		//Bisogna creare nuovo oggetto con sia nome categoria sia categoria in cui è spostato
		var categoryMoved = new Object();
		//Sistema qui
		//BiSOGNA SPOSTARE FUNZIONE DROP FUORI SEENò VIENE CHIAMATA SEMPRE
		categoryMoved.nomecat = subCategory.innerText;
		console.log(categoryMoved.nomecat)
		categoryMoved.nomefathercat = conference.nome_categoria;
		console.log(categoryMoved.nomefathercat)
		updates.push(categoryMoved)
		console.log(updates)
	}
	
	function blockDragging(listadabloccare) {
		listadabloccare.forEach( eltotblock => {
			
		})
	}

	function confirm() {
		
	}
	
	function deny() {
		
	}
	
	function save() {
		
	}
		  // Component that handles the form to create a conference
	function CreateCategoryForm(categoriesForm){
		this.categoriesForm = categoriesForm;
		this.registerClick = function(){

			// Obtain button and set click listener
			//this.categoriesForm.querySelector("input[type='button']").addEventListener('click', 
			document.getElementById("buttonhomepage").addEventListener('click', (e) => {
				// Obtain form form event
		        var form = e.target.closest("form");
		        
		        // Check form validity -- other validation could be added here if needed --
		        // https://developer.mozilla.org/es/docs/Web/API/HTMLSelectElement/checkValidity
		        if (form.checkValidity()) {
		          var self = this;
		           // if form is valid, then make post request
		          makeCall("POST", "CreateCategory", e.target.closest("form"),
		            function(req) {
						//form.submit();
						console.log(form)
		              if (req.readyState == XMLHttpRequest.DONE) {
		                var message = req.responseText;
		                if (req.status == 200) {
		                	// if the conference was created correctly, then update table
		                  topCategoriesList.show();
							//allCategoriesList.update();
							//Aggiornare anche menù a tendina del form
		                } else {
		                	// show message from server or a custom one
		                	if (message == "")
		                		message = "An issue has occurred";
		                	alert(message); //alert for demo purposes, could be displayed on the HTML
		                }
		              }
		            }
		          );
		        } else {
		          // if not valid, report it to user -- custom error messages could be added here -- 
		          // https://developer.mozilla.org/en-US/docs/Web/API/HTMLFormElement/reportValidity
		          form.reportValidity();
		        }
		      });
		}
	}
	
	function allCategoriesList(listaCat) {
		
		this.listaCat = listaCat;
		
		//this.update
		
	}

})();