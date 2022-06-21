import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';
import { Button, Dropdown, DropdownButton, Spinner, Tab, Tabs } from 'react-bootstrap';
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

enum Filters
{
	RADIOACTIVITY = "Radioactivity",
	TYPE = "Type",
	BLOCK = "Block",
	OCCURENCE = "Occurence"
}

type ElementContainerProps = 
{
 	element : Element;
	setCurrentElement : Dispatch<SetStateAction<Element | undefined>>;
	currentFilter : Filters
}

function ElementContainer({element, setCurrentElement, currentFilter} : ElementContainerProps)
{
	console.assert(element != undefined)

  	let borderStyle = "solid"
	{
		if(element.occurence == "Synthetic") borderStyle = "dotted"
		else if(element.occurence == "From Decay") borderStyle = "dashed"
		else console.assert(element.occurence == "Primordial")
	}
  

	let color = undefined;
	if(currentFilter == Filters.BLOCK){
		if(element.block == "P") color = "rgb(253, 255, 140)"
		// else if(element.block == "D") color = "deepskyblue"
		else if(element.block == "D") color = "rgb(153, 204, 255)"
		else if(element.block == "F") color = "rgb(155, 255, 153)"
		else {
			color = "rgb(255, 153, 153)"
			console.assert(element.block == "S")
		}
	}
	else if(currentFilter == Filters.TYPE)
	{
		if(element.type == "Nonmetal") color = "rgb(253, 255, 140)"
		// else if(element.block == "D") color = "deepskyblue"
		else if(element.type == "Metal") color = "lightgrey"
		else {
			color = "rgb(255, 153, 153)"
			console.assert(element.type == "Semimetal")
		}
	}
	else if(currentFilter == Filters.OCCURENCE)
	{
		if(element.occurence == "Primordial") color = "rgb(155, 255, 153)"
		else if(element.occurence == "From Decay") color = "rgb(255, 153, 153)"
		else 
		{
			console.assert(element.occurence == "Synthetic")
			color = "deepskyblue"
		} 
	}
	else 
	{
		console.assert(currentFilter == Filters.RADIOACTIVITY)
		if(element.radioactive) color = "red"
		else color = "green"
	}
	// {
	// 	color = "red";
	// 	if(element.radioactive) color = "red"
	// 	else color = "green"
	// }


	// let component = <></>

	// if(element.atomic_number == 57) //Lanthanum
	// {

	// }
	return <div 
		className="element_container_out"
		style={{
			gridRowStart: element.block == "F" ? 8 + (element.element_period - 5) : element.element_period,  
			gridColumnStart: element.block == "F" ? -element.element_group + 3 : element.element_group > 2 ? element.element_group + 1 : element.element_group
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
	currentFilter : Filters
}

function MainTable({setCurrentElement, currentFilter} : MainTableProps)
{

    let table = <div className="table_container">
      {
        elements.map((element, _) =>
		{
			
			return <>	
				<ElementContainer 
					element={element} 
					setCurrentElement={setCurrentElement} 
					currentFilter={currentFilter}
				/>
			</>

		}
        )
      }
    </div>

  
    return table;
}

function getNextElement(currentElement : Element | undefined)
{
	if(!currentElement) return undefined;
	if(currentElement.atomic_number == elements.length) return undefined;

	console.assert(elements[currentElement.atomic_number - 1] == currentElement)

	let nextElement = elements[currentElement.atomic_number]

	console.assert(nextElement.atomic_number == currentElement.atomic_number + 1, 
		`Current Element: ${currentElement.atomic_number} Next Element: ${nextElement.atomic_number}`)

	return nextElement;
}
function getPreviousElement(currentElement : Element | undefined)
{
	if(!currentElement) return undefined;
	if(currentElement.atomic_number == 1) return undefined;

	console.assert(elements[currentElement.atomic_number - 1] == currentElement)

	let previousElement = elements[currentElement.atomic_number - 2]

	console.assert(previousElement.atomic_number == currentElement.atomic_number - 1, 
		`Current Element : ${currentElement.atomic_number} Previous Element : ${previousElement.atomic_number}`)

	return previousElement;
}


function App() {

	const [show, setShow] = useState(false);
	const [currentElement, setCurrentElement] = useState<Element | undefined>(undefined);
	const [currentFilter, setCurrentFilter] = useState(Filters.BLOCK);
	const [loadingWikipedia, setLoadingWikipedia] = useState(false);

	React.useEffect(()=>{
		// console.log(currentElement)
		if(currentElement)
		{
			setShow(true);
			setLoadingWikipedia(true)
		}
			
	}, [currentElement]);

	const handleClose = () => {
		setShow(false);
		setCurrentElement(undefined)
	};
	const handleShow = () => setShow(true);

	return (
		<div style={{position: "relative"}}>
		<MainTable setCurrentElement={setCurrentElement} currentFilter={currentFilter}/>
		
		<div className="legend_container" id="legend_container">
			<div>
				<h3 className="occurence_legend" style={{borderStyle: "solid"}}>Primordial</h3>
				<h3 className="occurence_legend" style={{borderStyle: "dashed"}}>Decay</h3>
				<h3 className="occurence_legend" style={{borderStyle: "dotted"}}>Synthetic</h3>
			</div>
			<div>
				<DropdownButton
					align={{ lg: 'end' }}
					title={`Filter (${currentFilter}) `}
					id="filter"
				>
					<Dropdown.Item eventKey="1" onClick={() => setCurrentFilter(Filters.RADIOACTIVITY)}>Radioactivity</Dropdown.Item>
					<Dropdown.Item eventKey="2" onClick={() => setCurrentFilter(Filters.TYPE)}>Type</Dropdown.Item>
					<Dropdown.Item eventKey="3" onClick={() => setCurrentFilter(Filters.OCCURENCE)}>Occurence</Dropdown.Item>
					<Dropdown.Item eventKey="4" onClick={() => setCurrentFilter(Filters.BLOCK)}>Block (Default)</Dropdown.Item>
    			</DropdownButton>
			</div>
		</div>

		<Offcanvas show={show} onHide={handleClose} placement="end" style={{margin : 0, width : 600}}>
			<Offcanvas.Header closeButton>
				{/* <Offcanvas.Title>
					{currentElement?.name ?? "Select an element first!"}
				</Offcanvas.Title> */}
				<div style={{display : "flex", justifyContent : "space-between", width : 300}}>
					<Tabs defaultActiveKey="profile" id="uncontrolled-tab-example" className="mb-3">
						<Tab eventKey="home" title="Wikipedia">
						</Tab>
						<Tab eventKey="profile" title="Images">
						</Tab>
						{/* <Tab eventKey="contact" title="Contact">
						</Tab> */}
					</Tabs>
				</div>
			</Offcanvas.Header>
			<Offcanvas.Body style={{padding : 0, overflow: "hidden"}}>
				<iframe 
					id="element_frame" 
					style={{padding : 0}} 
					className="element_properties_frame" 
					src={currentElement ? "https://en.m.wikipedia.org/wiki/" + currentElement.name : ""} 
					onLoad={() => setLoadingWikipedia(false) }
				>
				</iframe>
				{
					loadingWikipedia ?
					<div style={{display : "flex" , justifyContent : "center", alignItems : "center", position : "absolute", width : "100%", height : "100%", top : 0}}>
						<Spinner animation="border" role="status" style={{width : 75, height : 75}}>
							<span className="visually-hidden">Loading...</span>
						</Spinner>
					</div>
					: <></>
				}
				
				{/* <div style={{position : "absolute", display : "flex", justifyContent : "space-around", height : "50px", bottom : 0}}>
					<p>Left</p>
					<p>Center</p>
					<p>Right</p>
				</div> */}
			</Offcanvas.Body>
			<Offcanvas.Header> {/*More like a footer */}
				<div style={{display : "flex", justifyContent : "space-around", width : "100%"}}>
					<a style={{cursor : "pointer"}}onClick={() => setCurrentElement(getPreviousElement(currentElement))}>
						{getPreviousElement(currentElement) ? "⬅️" + getPreviousElement(currentElement)?.name : ""}
					</a>
					<Offcanvas.Title>
						{currentElement?.name ?? "Select an element first!"}
					</Offcanvas.Title>
					<a style={{cursor : "pointer"}}onClick={() => setCurrentElement(getNextElement(currentElement))}>
						{getNextElement(currentElement) ? getNextElement(currentElement)?.name  + "➡️" : ""}
					</a>
				</div>
			</Offcanvas.Header>
		</Offcanvas>
		</div>
  );
}

export default App;
