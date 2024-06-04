import { Pagination } from "flowbite-react";
import React, {useEffect, useState} from 'react';
import '../../css/CourseList.css';
import Comment from "../CoursePage/Comment";

const CourseList = () => {
    // const [activeTab, setActiveTab] = useState('info');
    // const [activeTopic, setActiveTopic] = useState('info');
    const [firstPage, setFirstPage] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);

    // setCourse(test)
    useEffect(() => {
            const fetchUserInfo = async () => {
                const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses`, {credentials: 'include'});
                const data = await response.json();
                console.log(data);
                setFirstPage(data)
                setIsLoading(false);
            };
            fetchUserInfo();
        },
        []);

    const onPageChange = (page) => setCurrentPage(page);




    return ( <div>

        { isLoading ? <div>Loading...</div> :
        <div>

            {firstPage?.content.map((item, index) => (
// <div>{item.header}</div>

            <div className="card m-1 d-flex flex-column align-items-center h-100 productInstance">
                <div className="d-flex justify-content-center">
                    {/*<img src="${el.clPublicLink}" style="max-width: 390px; object-fit: contain;" className="card-img-top" alt="phone_photo"/>*/}

                </div>
                <div className="card-body d-flex flex-column align-items-center">
                    <h5 className="card-title card-model-title">${item.header + " " + item.rating}</h5>
                     <p className="card-text card-model-version">${item.duration}грн.</p>
                </div>
            </div>
            ))}

            <div className="flex courses-list-nav justify-content-center">
                <Pagination className="flex flex-row" currentPage={currentPage} totalPages={firstPage.totalPages}
                            onPageChange={onPageChange} showIcons/>
            </div>
        </div>
        }
        </div>

    );
};

export default CourseList;
