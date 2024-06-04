import { Pagination } from "flowbite-react";
import React, {useEffect, useState} from 'react';
import '../../css/CourseList.css';
import Comment from "../CoursePage/Comment";
import CourseComponent from "./CourseComponent";

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




    return (
        <div>

            { isLoading ? <div>Loading...</div> :
                // <div></div>
            <div className="cards-container d-flex flex-wrap mt-1">

                {firstPage?.content.map((item, index) => (
                    <CourseComponent item={item}></CourseComponent>
                ))}
            </div>
                // <div className="flex courses-list-nav justify-content-center">
                //     <Pagination className="flex flex-row" currentPage={currentPage} totalPages={firstPage.totalPages}
                //                 onPageChange={onPageChange} showIcons/>
                // </div>
            }
        </div>

    );
};

export default CourseList;
