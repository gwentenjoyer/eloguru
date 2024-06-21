import React, {useEffect, useState} from 'react';
import {Dropdown} from "react-bootstrap";

function Collapse({ label, children, id, courseId, topicId, userRole, isTeacherOwn, userEnrolled }) {
    const [isOpen, setIsOpen] = useState(false);
    const [isCompleted, setIsCompleted] = useState(false);

    const handleToggle = () => {
        setIsOpen(!isOpen);
    };

    const saveTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}/save_topic_progress`,
            {
                credentials: 'include',
                method: "POST"
            });
        if (response.status !== 201) {
            console.error("Failed to save data about topics");
        }
        setIsCompleted(true);
    };

    const removeTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}/remove_topic_progress`,
            {
                credentials: 'include',
                method: "POST"
            });
        if (response.status !== 204) {
            console.error("Failed to remove data about topics");
        }
        setIsCompleted(false);
    };

    const getCompletedTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}/completed`,
            {
                credentials: 'include'
            }
        );
        console.log("response", response)
        if (response.status == 200) {
            const data = await response.json();
            let isTopicsComplete = data.completedTopicIds.includes(topicId);
            setIsCompleted(isTopicsComplete);
        }
        else if (response.status == 204){
            setIsCompleted(false)
            }
        else {
            console.error("Failed to fetch completed topics");
        }
    };

    const handleDeleteTheme = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}/delete`, { credentials: 'include', method: "POST" });
        if (response.status !== 200) {
            console.log(response);
            console.error("Failed to delete");
        }
        console.log("deleted");
    };

    const handleEditTheme = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${courseId}/topics/${topicId}`, {credentials: 'include', method: "PUT"});
        if (response.status !== 200) {
            console.log(response);
            console.error("Failed to edit");
        }
        console.log("edited");
    };

    useEffect(() => {
        getCompletedTopics();
    }, [courseId, topicId]);

    return (
        <div className="card">
            <div className="card-header d-flex justify-content-between align-items-center" id={`heading${id}`}>
                <div className="me-1 ms-3">
                    {userRole == "STUDENT" &&
                        <>
                            { isCompleted && userEnrolled &&
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" onClick={removeTopics} color={"blue"} height="24" fill="currentColor" className="bi bi-check-square" viewBox="0 0 16 16">
                                    <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                    <path d="M10.97 4.97a.75.75 0 0 1 1.071 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                                </svg>
                            }
                            { !isCompleted && userEnrolled &&
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" onClick={saveTopics} color={"blue"} height="24" fill="currentColor" className="bi bi-square" viewBox="0 0 16 16">
                                    <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                </svg>
                            }
                        </>
                    }
                </div>
                <div className="flex-grow-1">
                    <h5 className="mb-0 d-inline" style={{ cursor: "pointer" }} onClick={userRole === "STUDENT" || isTeacherOwn ? handleToggle : () => {}}>
                        <button
                            className="btn btn-link text-dark colapsium"
                            aria-expanded={isOpen}
                            style={{ paddingRight: "91%", borderBottom: "none", textDecoration: "none" }}
                            aria-controls={`collapse${id}`}
                        >
                            {label}
                        </button>
                    </h5>
                </div>
                {userRole === "TEACHER" && isTeacherOwn && (
                    <div className="mx-3">
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
                )}
            </div>
            {isOpen && (userEnrolled || isTeacherOwn) && (
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
