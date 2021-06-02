
(function() {

	// Component that handles the Conferences Table
	var topCategoriesList = new TopCategoriesList(document.getElementById("catalogdiv")
		,document.getElementById("cataloglist"));

	// Component that handles the form to create a conference
	var createCategoryForm = new CreateCategoryForm(document.getElementById("createcategoryform"));

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
	function makeCall(method, url, formElement, cback, reset = true) {
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

	  }

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
						console.log(conferencesToShow.length);
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

				//Qui dentro va nested iterazione se gson non mantiene albero
		      categoriesArray.forEach(function(conference) { // self visible here, not this
		        //Create a row for each conference
				var category = document.createElement("li")
				var cat = document.createElement("ol")
		        category.innerText = conference.nome_categoria;
				category.draggable = true;
				category.ondrop = true;
				
				let startElement;
				
				category.addEventListener("dragstart", function(event) {
					startElement = event.target.closest("li");
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
					var dest = event.target.closest("li");
					
				})
				
				
				catalog.appendChild(category);
				category.appendChild(cat)
				conference.subCategories.forEach(sub=> {
					var subCategory = document.createElement("li");
					subCategory.innerText = sub.nome_categoria;
					subCategory.draggable = true
					cat.appendChild(subCategory);
					//Aggiungere qui funzione ricorsiva
					//this.update(conference.subCategories);
				})
				/*var subs = category.subCategories;
				subs.forEach(sub => {
					var subCategory = document.createElement("li");
					subCategory.innerText = sub.nome_categoria;
					category.appendChild(subCategory);
				})*/
		        /*nameCell = document.createElement("td");
		        nameCell.textContent = conference.name;
		        row.appendChild(nameCell);

		        datecell = document.createElement("td");
		        datecell.textContent = conference.date;
		        row.appendChild(datecell);

				// Add row to table body
		        self.listcontainerbody.appendChild(row);*/
		      });
		      
			  /*resetArrows(this.listcontainer.querySelectorAll('th'));
		      this.listcontainer.style.visibility = "visible";*/
		    }
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
			this.categoriesForm.querySelector("input[type='button']").addEventListener('click', 
				(e) => {
				// Obtain form form event
		        var form = e.target.closest("form");
		        
		        // Check form validity -- other validation could be added here if needed --
		        // https://developer.mozilla.org/es/docs/Web/API/HTMLSelectElement/checkValidity
		        if (form.checkValidity()) {
		          var self = this;
		           // if form is valid, then make post request
		          makeCall("POST", "CreateCategory", form,
		            function(req) {

		              if (req.readyState == XMLHttpRequest.DONE) {
		                var message = req.responseText;
		                if (req.status == 200) {
		                	// if the conference was created correctly, then update table
		                  topCategoriesList.show();
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

})();