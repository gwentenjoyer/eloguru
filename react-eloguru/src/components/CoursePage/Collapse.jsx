import React, { useState } from 'react';
import {Dropdown} from "react-bootstrap";

function Collapse({ label, children, id, courseId, topicId, userRole }) {
    const [isOpen, setIsOpen] = useState(false);

    const handleToggle = () => {
        setIsOpen(!isOpen);
    };

    const handleDeleteTheme = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}/delete`, {credentials: 'include', method: "POST"});

        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("deleted")
    }

    const handleEditTheme = async () => {
        // const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}`, {credentials: 'include', method: "PUT"});
        const response = {status: 200}
        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("edited")
    }

    return (
        <div className="card">
            <div className="card-header d-flex flex-row justify-content-between" id={`heading${id}`}>
                <div className={"mx-3"}>

                <h5 className="mb-0"
                    onClick={handleToggle}>
                    <button
                        className="btn btn-link text-dark"
                        aria-expanded={isOpen}
                        aria-controls={`collapse${id}`}
                    >
                        {label}
                    </button>

                </h5>
                </div>
                {userRole === "TEACHER"?
                        <div className={"mx-3"}>
                            <Dropdown>
                                <Dropdown.Toggle variant="success" id="dropdown-basic">
                                    More
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item href="#/action-1" onClick={handleEditTheme}>Edit</Dropdown.Item>
                                    <Dropdown.Item href="#/action-2" onClick={handleDeleteTheme}>Delete</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                :<></>}
            </div>
            {isOpen && (
                <div id={`collapse${id}`} className="collapse show" aria-labelledby={`heading${id}`}>
                    <div className="card-body">
                        {children}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Collapse;
