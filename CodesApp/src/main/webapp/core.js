/*
 * Copyright (C) 2015 Bryan W. Snipes
 * 
 * This file is part of the JDistil web application framework.
 * 
 * JDistil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JDistil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDistil.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Sets the value of a specified form field.
 */
function setValue(formId, fieldName, fieldValue) {

	if (fieldName != null && fieldName != '' &&
		fieldValue != null && fieldValue != '') {
			
		// Get form
		var form = getForm(formId);
		
		if (form != null) {
		
			// Get field
			var field = form.elements[fieldName];
		
			// Set field value
			if (field != null) {
				field.value = fieldValue;
			}
		}
	}
}

/**
 * 	Submits a forms values.
 */
function submitAction(formId, actionName, confirmationMessage) {

	// Initialize submit indicator
	var doSubmit = true;
	
	// Set submit indicator if a confirmation message is provided	
	if (confirmationMessage != null && confirmationMessage != '') {
		doSubmit = confirm(confirmationMessage);
	}
	
	if (doSubmit) {
	
		// Get form
		var form = getForm(formId);
		
		if (form != null) {

			// Disable form buttons
			for (var index = 0; index < form.elements.length; index++) {
				
				// Get current element
				var element = form.elements[index];
				
				// Disable button elements
				if (element.type == 'button') {
					element.disabled = true;
				}
			}
			
			// Set servlet action
			if (actionName != null && actionName != '') {
			  form.ACTION_ID.value=actionName;
			}
		
			// Submit form
			form.submit();
		}
	}
}

/**
 * Moves items from a source list to a destination list and
 * creates hidden fields based on all moved items.
 */
function addItems(formId, fieldName, sourceId, destinationId, moveAll) {

	// Validate parameters
	if (fieldName != null && sourceId != null && destinationId != null) {

		// Get form
		var form = getForm(formId);
		
		// Retrieve list elements
		var source = document.getElementById(sourceId);
		var destination = document.getElementById(destinationId);
		
		// Validate document elements
		if (form != null && source != null && destination != null) {
		
			for (index = 0; index < source.length; index++) {
	
				// Get next item
				var item = source.options[index];
	
				if (item.selected || moveAll) {
	
					// Get item value as field value
					var fieldValue = item.value;
	
					// Initialize new hidden element
					var hiddenElement = null;
	
					try {
	
						// Create a new hidden element
						hiddenElement = document.createElement('<input type="hidden" name="' + fieldName + '" value="' + fieldValue + '" />');
					}
					catch (e) {
	
						// Create new hidden element               
						hiddenElement = document.createElement('input');
	
						// Populate hidden element
						hiddenElement.setAttribute('type', 'hidden');
						hiddenElement.setAttribute('name', fieldName);
						hiddenElement.setAttribute('value', fieldValue);
					}
	
					// Append hidden element to form
					form.appendChild(hiddenElement);
				}
			}
		}
		
		// Move items in select lists		
		moveItems(source, destination, moveAll)
	}
}

/**
 * Moves items from a source list to a destination list and
 * removes hidden fields based on all moved items.
 */
function removeItems(formId, fieldName, sourceId, destinationId, moveAll) {

	// Validate parameters
	if (fieldName != null && sourceId != null && destinationId != null) {

		// Get form
		var form = getForm(formId);
		
		// Retrieve list elements
		var source = document.getElementById(sourceId);
		var destination = document.getElementById(destinationId);
		
		// Validate document elements
		if (form != null && source != null && destination != null) {

			// Get all hidden elements
			var hiddenElements = document.getElementsByName(fieldName);
	
		    if (hiddenElements != null) {
		    
	  			for (index = 0; index < source.length; index++) {
	
		  			// Get next item
			  		var item = source.options[index];
	
				  	if (item.selected || moveAll) {
	
					  	// Get item value as field value
	  					var fieldValue = item.value;
		
		  				for (elementIndex = 0; elementIndex < hiddenElements.length; elementIndex++) {
	
			  				// Get next hidden element
	  						var hiddenElement = hiddenElements[elementIndex];
		
	  						// Remove hidden element if values match
		  					if (hiddenElement.value == fieldValue) {
			  					hiddenElement.parentNode.removeChild(hiddenElement);
				  				break;
					  		}
					  	}
					}
				}
			}

	  	// Move items in select lists		
  		moveItems(source, destination, moveAll)
		}
	}
}

/**
 * Moves selected items from a source list to a destination list.
 */
function moveItems(source, destination, moveAll) {

  if (source.selectedIndex != -1 || moveAll == true) {

	  // Create array for new destination values
	  newDestination = new Array(destination.options.length);
	
	  // Initialize new index
	  var newIndex = 0;
	
	  // Add current destination value to new destination values array
	  for (var destinationIndex = 0; destinationIndex < destination.options.length; destinationIndex++) {
	
	    // Get next destination option
	    var destinationOption = destination.options[destinationIndex];
	
	    if (destinationOption != null) {
	
	      // Get option properties
	      var text = destinationOption.text;
	      var value = destinationOption.value;
	      var defaultSelected = destinationOption.defaultSelected;
	      var selected = destinationOption.selected;
	
	      // Create new option
	      newDestination[newIndex] = new Option(text, value, defaultSelected, selected);
	
	      // Increment new index
	      newIndex++;
	    }
	  }
	
	  // Add choosen source items to new destination values array
	  for (var sourceIndex = 0; sourceIndex < source.options.length; sourceIndex++) {
	
	    // Get next source option
	    var sourceOption = source.options[sourceIndex];
	
	    if (sourceOption != null && (sourceOption.selected == true || moveAll)) {
	
	      // Get option properties
	      var text = sourceOption.text;
	      var value = sourceOption.value;
	      var defaultSelected = sourceOption.defaultSelected;
	      var selected = sourceOption.selected;
	
	      // Assign to array
	      newDestination[newIndex] = new Option(text, value, defaultSelected, selected);
	
	      // Increment new index
	      newIndex++;
	    }
	  }
	
	  // Sort new destination array values
	  newDestination.sort(compareOptionValues);
	
	  // Populate the destination list using new destination array values
	  for (var desintationIndex = 0; desintationIndex < newDestination.length; desintationIndex++) {
	
	    if (newDestination[desintationIndex] != null) {
	      destination.options[desintationIndex] = newDestination[desintationIndex];
	    }
	  }
	
	  // Remove items from source list
	  for (var sourceIndex = source.options.length - 1; sourceIndex >= 0; sourceIndex--) {
	
	    // Get next source option
	    var sourceOption = source.options[sourceIndex];
	
	    // Remove item from list
	    if (sourceOption != null && (sourceOption.selected == true || moveAll)) {
	      source.options[sourceIndex] = null;
	    }
	  }
  }
}

/**
 * Compares two values for sorting purposes.
 */
function compareOptionValues(a, b) {

  var aValue = parseInt(a.value, 36);
  var bValue = parseInt(b.value, 36);

  return aValue - bValue;
}

 /**
  * Returns a form given a form ID or the first 
  * available form if a form ID is not specified.
  */
 function getForm(formId) {
 	
 	// Initialize form
 	var form = null;
 	
 	// Get form by ID if provided
 	if (formId != null && formId != '') {
 		form = document.getElementById(formId);
 	}

 	// Get first available form
 	if (form == null){
 		form = document.forms[0];
 	}

 	return form;
 }

/**
 * Toggles the display of a menu's items.
 */
function toggleMenu(menuId) {
	
	var menu = document.getElementById(menuId);

	if (menu != null) {

		var elements = document.getElementsByTagName('div');

		for (index = 0; index < elements.length; index++) {

			var element = elements[index];

			if (element.className == 'menuItems' && element != menu) {
				element.style.display = 'none';
			}
		}

		var display = menu.style.display;

		if (display == '' || display == 'none') {
			menu.style.display = 'block';
		}
		else {
			menu.style.display = 'none';
		}
	}
}

/**
 * Toggles the display of components contained in a group component.
 */
function toggleGroup(groupFieldId, groupLinkId, groupViewId, hideLabel, showLabel) {
	
	var groupField = document.getElementById(groupFieldId);
	var groupLink = document.getElementById(groupLinkId);
	var groupView = document.getElementById(groupViewId);

	if (groupField != null && groupLink != null && groupView != null) {

		var display = groupView.style.display;

		if (display == 'none') {
			groupField.value = '1';
			groupLink.text = hideLabel;
			groupView.style.display = 'inline-block';
		}
		else {
			groupField.value = '0';
			groupLink.text = showLabel;
			groupView.style.display = 'none';
		}
	}
}

/**
 * Clears filter view components.
 */
function clearFilter(formId, filterViewId) {
	
	var filterView = document.getElementById(filterViewId);

	if (filterView != null) {
		
		var inputElements = filterView.getElementsByTagName('input');
		
		if (inputElements != null) {
			
			for (var index = 0; index < inputElements.length; index++) {
				
				var inputElement = inputElements[index];
				
				switch (inputElement.type) {
					case "text":
					case "date":
					case "datetime":
					case "datetime-local":
					case "time":
					case "month":
					case "week":
					case "number":
					case "range":
					case "search":
					case "email":
					case "tel":
					case "url":
						inputElement.value = "";
						break;
					case "radio":
					case "checkbox":
						child.checked = false;
						break;
				}
			}
		}

		var textAreaElements = filterView.getElementsByTagName('textarea');
		
		if (textAreaElements != null) {
			
			for (var index = 0; index < textAreaElements.length; index++) {

				textAreaElements[index].value = "";
			}
		}

		var selectElements = filterView.getElementsByTagName('select');
		
		if (selectElements != null) {
			
			var sourcePrefix = 'source_';
			var destinationPrefix = 'destination_';
			
			for (var index = 0; index < selectElements.length; index++) {
				
				var selectElement = selectElements[index];
				
				var selectId = selectElement.id;
				
				var position = selectId.indexOf(destinationPrefix);
				
				if (position == 0 && selectId.length > destinationPrefix.length) {
					
					var fieldName = selectId.substring(destinationPrefix.length);
					
					var sourceId = sourcePrefix + fieldName;
					
					removeItems(formId, fieldName, selectId, sourceId, true);
				}
				else {
					
					var dataType = selectElement.getAttribute('data-type');
					
					if (dataType == null || dataType != 'operator') {
						
						selectElement.selectedIndex = -1;
					}
				}
			}
		}
	}
}
