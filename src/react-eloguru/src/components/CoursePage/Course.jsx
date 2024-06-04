import React, {useEffect, useState} from 'react';
import '../../css/CourseDetailPage.css';
import Collapse from "./Collapse";

const Course = () => {
    const [activeTab, setActiveTab] = useState('info');
    const [activeTopic, setActiveTopic] = useState('info');
    const [course, setCourse] = useState();
    const [isLoading, setIsLoading] = useState(true);

    // setCourse(test)
    useEffect(() => {
            const fetchUserInfo = async () => {
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/4`, {credentials: 'include'});
                const data = await response.json();
                console.log(data);
                // {
                //     "surname":"nick"
                //     ,"name":"sur"
                //     ,"id":0,
                //     "categories":["A1"],
                //     "age":0
                // }
                setCourse(data);
                // let test = {name: "sdf", description: "sdf", rating: 5, hours: "5"}
                // setCourse(test);
                setIsLoading(false);
            };

            fetchUserInfo();
        },
        []);

    // const showLoaded = (content) => {
    function showLoaded(content){
        console.log(isLoading)
        return isLoading || !course ? "Loading..." : content;
    }

    const fetchTopics = async () => {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses/4/topics`, {credentials: 'include'});
        console.log(response)
        return response;
    }

    const printRate = (value) => {
        // const num = value.
        return parseFloat(value).toFixed(1);
    }
    // function Collapse({ label, children }) {
    //     const [isOpen, setIsOpen] = useState(false);
    //
    //     const handleToggle = () => {
    //         setIsOpen(!isOpen);
    //     };
    //
    //     return (
    //         // <div>
    //         //     <button onClick={handleToggle}>{title}</button>
    //         //     {isOpen && <div>{children}</div>}
    //         // </div>
    //     <div className="card">
    //         <div className="card-header">
    //             <h5 className="mb-0">
    //             <button className="btn btn-link" data-toggle="collapse" onClick={handleToggle}
    //                         data-target="#collapseOne"
    //                         aria-expanded="true" aria-controls="collapseOne">
    //                     {label}
    //                 </button>
    //             </h5>
    //         </div>
    //         {isOpen && children}
    //
    //     </div>
    // )
    //     ;
    // }

    return (
        <div className="course-detail-page">
            <div className="course-header">
                {isLoading ? <div>Loading...</div> :
                    <div className="course-info">
                        <h1>{(course?.header)}</h1>
                        <h5>{(course?.description)}</h5>
                        <p>Rate: {printRate(course?.rating)} / 5</p>

                        <p>Days: {(course?.durationDays)}</p>
                    </div>
                }
                <button className="sign-up-button">Enroll</button>
            </div>
            <div className="course-tabs">
                <button onClick={() => setActiveTab('info')} className={activeTab === 'info' ? 'active' : ''}>Info</button>
                <button onClick={() => setActiveTab('comments')} className={activeTab === 'comments' ? 'active' : ''}>Comments</button>
                <button onClick={() => setActiveTab('themes')} className={activeTab === 'themes' ? 'active' : ''}>Themes</button>
            </div>
            <div className="course-content">
                {activeTab === 'info' && <div>{"test"}</div>}
                {activeTab === 'comments' && <div>Comments section...</div>}
                {activeTab === 'themes' && <div>
                    <div id="accordion">
                        {
                            // console.log(fetchTopics())

                        }
                        {/*<Collapse> sdf</Collapse>*/}
                        {/*{course?.topics.map((item) => (*/}
                        {/*    // <div key={item}>{item}</div>*/}
                        {/*    <Collapse label={item.label}>*/}
                        {/*        <div className="collapse" aria-labelledby="headingThree"*/}
                        {/*             data-parent="#accordion">*/}
                        {/*            <div className="card-body">*/}
                        {/*                {item.description}*/}
                        {/*            </div>*/}
                        {/*        </div>*/}
                        {/*    </Collapse>*/}

                        {/*))}*/}
                        {course?.topics.map((item, index) => (
                            <Collapse key={index} label={item.label} id={index}>
                                <div>
                                    {item.description}
                                </div>
                            </Collapse>
                        ))}
                    </div>

                </div>}
            </div>
        </div>
    );
};

export default Course;
