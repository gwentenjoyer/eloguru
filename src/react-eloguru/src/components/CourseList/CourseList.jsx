import React, { useEffect, useState } from 'react';
import '../../css/CourseList.css';
import CourseComponent from "./CourseComponent";
import { Pagination } from "flowbite-react";

const CourseList = () => {
    const [firstPage, setFirstPage] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(2);

    const fetchUserInfo = async (page, size) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses?page=${page}&size=${size}`, { credentials: 'include' });
            const data = await response.json();
            console.log(data)
            setFirstPage(data);
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchUserInfo(currentPage - 1, pageSize);
    }, [currentPage, pageSize]);

    const onPageChange = (page) => {
        setCurrentPage(page);
    };

    return (
        <div>
            <div className="mainwrapper d-flex flex-row pt-4 mb-3">
                <aside className="catalog-sidebar me-4 ms-3">
                    <div className="sidebar-title">
                        <b>Критерії вибору товару</b>
                        <hr/>

                        <hr/>
                        <b className="sidebar-parag-title">Сортувати за:</b>
                        <select className="form-select" id="sortby" aria-label="Default select example"
                            // onChange="sortPhones()"
                        >
                            <option value="0" key={0}>Старішим</option>
                            <option value="-1" key={-1}>Новішим</option>
                            <option value="1" key={1}>Зростанням</option>
                            <option value="2" key={2}>Спаданням</option>
                        </select>
                    </div>
                </aside>
                {isLoading ? <div>Loading...</div> :
                    <div>
                        <div className="cards-container d-flex flex-wrap mt-1">
                            {firstPage?.content.map((item, index) => (
                                <CourseComponent item={item} key={index}></CourseComponent>
                            ))}
                        </div>
                        <div className="flex courses-list-nav justify-content-center">
                            <Pagination
                                className="flex flex-row"
                                currentPage={currentPage}
                                totalPages={firstPage?.totalPages || 1}
                                onPageChange={onPageChange}
                                showIcons
                            />
                        </div>
                    </div>
                }
            </div>
        </div>
    );
}
;

export default CourseList;
