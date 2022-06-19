import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';
import { Button, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import OffcanvasHeader from 'react-bootstrap/OffcanvasHeader'
import Offcanvas from 'react-bootstrap/Offcanvas'
import OffcanvasTitle from 'react-bootstrap/OffcanvasTitle'
import OffcanvasBody from 'react-bootstrap/OffcanvasBody'

import elements from './elements.json';
import { assert } from 'console';

type Element = 
{
	symbol : string;
	occurence : string;
	radioactive : boolean;
	name : string;
	element_group : number;
	block : string;
	atomic_number : number;
	type : string;
	element_period : number;
	atomic_mass : number;
}

type ElementContainerProps = 
{
 	element : Element;
	setCurrentElement : Dispatch<SetStateAction<Element | undefined>>
}

function ElementContainer({element, setCurrentElement} : ElementContainerProps)
{
	console.assert(element != undefined)

  	let borderStyle = "solid"
	{
		if(element.occurence == "Synthetic") borderStyle = "dotted"
		else if(element.occurence == "From Decay") borderStyle = "dashed"
		else console.assert(element.occurence == "Primordial")
	}
  

	let color = "rgb(255, 153, 153)";
	{
		if(element.block == "P") color = "rgb(253, 255, 140)"
		// else if(element.block == "D") color = "deepskyblue"
		else if(element.block == "D") color = "rgb(153, 204, 255)"
		else if(element.block == "F") color = "rgb(155, 255, 153)"
		else console.assert(element.block == "S")
	}


	return <div 
		className="element_container_out"
		style={{
			gridRowStart: element.block == "F" ? 8 + (element.element_period - 5) : element.element_period,  
			gridColumnStart: element.block == "F" ? -element.element_group + 2: element.element_group
		}}
	>
		<div id={element.symbol} key={element.atomic_number} className="element_container" 
			style={
			{
				backgroundColor: color, 
				borderStyle: borderStyle,
				// justifyContent : "center", 
				flexDirection : "column"
			}}
			onClick={() => setCurrentElement(element)} 
			>
			<h1 className="atomic_number">&nbsp;{ element.atomic_number  }</h1>
			<h1 className="element_symbol">{ element.symbol }</h1>
			<h1 className="atomic_mass">{ element.atomic_mass }&nbsp;</h1>
			<div style={{display : "flex", justifyContent : "center"}}>
				<span className="tooltiptext">{element?.name}</span>
			</div>
		</div>
	</div>
}


type MainTableProps =
{
	setCurrentElement : Dispatch<SetStateAction<Element | undefined>>
}

function MainTable({setCurrentElement} : MainTableProps)
{

    let table = <div className="table_container">
      {
        elements.map((element, _) =>
          <ElementContainer element={element} setCurrentElement={setCurrentElement}/>
        )
      }
    </div>

  
    return table;
}


function App() {

	const [show, setShow] = useState(false);
	const [currentElement, setCurrentElement] = useState<Element | undefined>(undefined);

	React.useEffect(()=>{
		// console.log(currentElement)
		if(currentElement)
			setShow(true);
	}, [currentElement]);

	const handleClose = () => {
		setShow(false);
		setCurrentElement(undefined)
	};
	const handleShow = () => setShow(true);

	return (
		<div style={{position: "relative"}}>
		<MainTable setCurrentElement={setCurrentElement}/>
		
		<div className="legend_container" id="legend_container">
			<h3 className="occurence_legend" style={{borderStyle: "solid"}}>Primordial</h3>
			<h3 className="occurence_legend" style={{borderStyle: "dashed"}}>Decay</h3>
			<h3 className="occurence_legend" style={{borderStyle: "dotted"}}>Synthetic</h3>
		</div>

		<Offcanvas show={show} onHide={handleClose} placement="end" style={{margin : 0, width : 600}}>
			<Offcanvas.Header closeButton>
			<Offcanvas.Title>{currentElement?.name ?? "Select an element first!"}</Offcanvas.Title>
			<Button variant="danger">
				Wikipedia
			</Button>
			<Button variant="primary">
				Google
			</Button>
			<Button variant="primary">
				Bing
			</Button>
			</Offcanvas.Header>
			<Offcanvas.Body style={{padding : 0, overflow: "hidden"}}>
				<iframe 
					id="element_frame" 
					style={{padding : 0}} 
					className="element_properties_frame" 
					src={currentElement ? "https://en.m.wikipedia.org/wiki/" + currentElement.name : ""} 
				/>
			</Offcanvas.Body>
		</Offcanvas>
		</div>
  );
}

export default App;
