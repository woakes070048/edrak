
    var agt=navigator.userAgent.toLowerCase();
  var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));

  
  
  function clear_Model(prefix,parent){
	  	var obj=getDiv(parent);
	  	prefix+="_";
	  	if (!obj) return ;
	  
	  	for (var i=0;i<obj.childNodes.length;i++){
	  		var child=obj.childNodes[i];
	  		
	  		if (!isEmpty(child.id)){
	  			var str=new String(child.id);

	  			if (str.indexOf(prefix)>=0){
	  				setValue(child.id,"");
	  			}
	  		}
	  		
	  		if (child.childNodes.length>0) clearModel2(prefix,child);
	  	}
	  	
	  }
  
  
  
   function clearModel(prefix,parent){
  	var obj=getDiv(parent);
  	prefix+=".";
  	if (!obj) return ;
  
  	for (var i=0;i<obj.childNodes.length;i++){
  		var child=obj.childNodes[i];
  		
  		if (!isEmpty(child.id)){
  			var str=new String(child.id);

  			if (str.indexOf(prefix)>=0){
  				setValue(child.id,"");
  			}
  		}
  		
  		if (child.childNodes.length>0) clearModel2(prefix,child);
  	}
  	
  }
  
   function clearModel2(prefix,parentObj){
  	
  	for (var i=0;i<parentObj.childNodes.length;i++){
  		var child=parentObj.childNodes[i];
  		
  		if (!isEmpty(child.id)){
  			var str=new String(child.id);
  			
  			if (str.indexOf(prefix)>=0){
  				setValue(child.id,"")
  			}
  		}
  		
  		if (child.childNodes.length>0) clearModel2(prefix,child);
  		
  	}
  	
  	
  }
  
  function getModel(prefix,parent){
  	var obj=getDiv(parent);
  	var ret={};
  	
  	if (!obj) return ret;
  
  	for (var i=0;i<obj.childNodes.length;i++){
  		var child=obj.childNodes[i];
  		
  		if (!isEmpty(child.id)){
  			var str=new String(child.id);
  			
  			if (str.indexOf(prefix)>=0){
  				var val=str.substring(prefix.length+1,str.length);
  				ret[val]=getValue(child.id);
  			}
  		}
  		
  		if (child.childNodes.length>0) ret=getModel2(prefix,ret,child);
  	}
  	//debugMe(ret);
  	return ret;
  }
  
  function getModel2(prefix,ret,parentObj){
  	
  	for (var i=0;i<parentObj.childNodes.length;i++){
  		var child=parentObj.childNodes[i];
  		
  		if (!isEmpty(child.id)){
  			var str=new String(child.id);
  			
  			if (str.indexOf(prefix)>=0){
  				var val=str.substring(prefix.length+1,str.length);
  				ret[val]=getValue(child.id);
  			}
  		}
  		
  		if (child.childNodes.length>0) ret=getModel2(prefix,ret,child);
  		
  	}
  	
  	return ret;
  }
  
  function setModel(prefix,model){
  	for (var prop in model){
  		
  		setValue(prefix+"."+prop,model[prop]);
  		
  	}
  }
  
  
  function set_Model(prefix,model){
	  	for (var prop in model){
	  		
	  		setValue(prefix+"_"+prop,model[prop]);
	  		
	  	}
  }
  
  
  
  function setValue(div,value){
  	if (getDiv(div)) DWRUtil.setValue(div,value);
  }
  function isVacationDay(strDate){
  	
  	//return true if fri or sat 5,6
  	//return false if else
  	
  	if (isEmpty(strDate)) return false; 
  	
  	var temp=strDate.split("/");
  	if (temp==undefined || temp.length!=3) return false;
  	
  	var date=new Date();
  	var y=new Number(temp[2]);
  	var m=(new Number(temp[1]))-1;
  	var d=new Number(temp[0]);
  	
  	date.setFullYear(y);
  	date.setMonth(m);
  	date.setDate(d);
  	
  	if (date.getDay()==5 || date.getDay()==6) return true;
  	
  	return false;
  }
  function compareDate(strDate1,strDate2){
  	
  	// accept date as string in the form dd/MM/yyyy
  	//return 0 if equal
  	//return 1 if strDate1 greater(after) strDate2
  	//return -1 if strDate1 smaller(before) strDate2
  	//return -2 if any date is empty or not an date
  	
  	if (isEmpty(strDate1) || isEmpty(strDate2)) return -2;
  	
  	var temp1=strDate1.split("/");
  	
  	if (temp1==undefined || temp1.length!=3) return -2;
  	
  	var temp2=strDate2.split("/");
  	
  	if (temp2==undefined || temp2.length!=3) return -2;
  	
  	try {
	
  		var y1=new Number(temp1[2]);
  		var m1=new Number(temp1[1]);
  		var d1=new Number(temp1[0]);
  		
  		
  		var y2=new Number(temp2[2]);
  		var m2=new Number(temp2[1]);
  		var d2=new Number(temp2[0]);
  		
  		
  		if (y1>y2) return 1;
  		if (y1<y2) return -1;
  		
  		if (m1>m2) return 1;
  		if (m1<m2) return -1;
  		
  		if (d1>d2) return 1;
  		if (d1<d2) return -1;
  		
  		return 0;
  		
  		
  	
  	}catch (e) {
  		
  	}
  	
  	return -2;
  }
  
  // #########################
  
  function isEmpty(str){
	
	if (str==null || str==undefined) return true;
	
	if (trim(str)=="") return true;
	
	return false;
}


// #########################
function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
	
	do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
			} while (obj = obj.offsetParent);
			
			}
	var ret={};
	ret.curleft=curleft;
	ret.curtop=curtop;	
	return ret;
	
	}

// #########################

function debugMe(id) {

alert(DWRUtil.toDescriptiveString(id),0,4);
}
//  ######################  
    function getDiv(id) {
    	return document.getElementById(id);
    }
    
     function getDivs(id) {
    	return document.getElementsByName(id);
    }
//  ######################  

function IsPosInteger(strString){
 var strValidChars = "0123456789";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

	if (IsNumeric(strString) ){
	 	
	 	var x=new Number(trim(strString));
	 	if (x==0) return false;
	 
	 }
   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;

}

//
function IsNumeric(strString)
   //  check for valid numeric strings	
   {
   var strValidChars = "0123456789.-";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;
   }
///#############################

function divPutValue(key , val) {

if (val==null) return;

if (is_ie) {

getDiv(key).innerText=val;
}else {

getDiv(key).textContent=val;

}

}

//#################################

function getDivValue(key) {

if (is_ie) {

return getDiv(key).innerText;
}else {

return getDiv(key).textContent;

}

}


//################################

function divPutHTMLValue(key , val) {

	getDiv(key).innerHTML=val;

}

////////#########################

function getValue(key) {

	return DWRUtil.getValue(key);
}

////////#########################
function trim(str) { return str.replace(/^\s+|\s+$/g, ''); };
////////#########################

function multichoice(_object)
{
// read status of radio button <m> set in form <n> and return value of selected button
// _object=document.forms[n].elements[m];

_object=getDivs(_object);

for(var i=0;i<_object.length;i++)
{

if(_object[i].checked==true)
{
return _object[i].value;
}
}
return 'null';
}

    