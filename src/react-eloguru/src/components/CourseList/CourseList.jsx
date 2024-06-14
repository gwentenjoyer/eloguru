import React, { useEffect, useState } from 'react';
import '../../css/CourseList.css';
import CourseComponent from "./CourseComponent";
import { Pagination } from "flowbite-react";
import {useLocation} from "react-router-dom";
import Button from "react-bootstrap/Button";

const CourseList = () => {
    const [firstPage, setFirstPage] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(3);

    const location  = useLocation();

    const fetchUserInfo = async (page, size) => {
        try {

            const queryParams = new URLSearchParams(location.search);
            const label = queryParams.get('label');
            // console.log(`${process.env.REACT_APP_SERVER_URL}/courses?page=${page}&size=${size}${label ? `&label=${label}`: ""}`);
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/courses?page=${page}&size=${size}${label ? `&label=${label}`: ""}`, { credentials: 'include' });
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
    }, [currentPage, pageSize, location]);

    const onPageChange = (page) => {
        setCurrentPage(page);
    };

    return (
        <div>
            <div>
{/*<div>*/}
{/*    Create course*/}
{/*</div>*/}
            <div className="mainwrapper d-flex flex-row pt-4 mb-3">
                <aside className="catalog-sidebar me-4 ms-3">
                    <div className="sidebar-title d-flex flex-column justify-content-center">
                        <div>

                        <b>Filter courses by creterea:</b>
                        <hr/>


                        {/*<label className="day">asdf*/}
                        {/*</label>*/}
                        {/*<input*/}
                        {/*    className="formFieldCheckbox"*/}
                        {/*    type="checkbox"*/}
                        {/*    name="hasAgreed"*/}
                        {/*    value={hasAgreed}*/}
                        {/*    onChange={handleCheckChange}*/}
                        {/*/>*/}
                        <div className={"d-flex flex-row"}>

                            <label htmlFor="filterDays" className={"me-2 align-bottom"}>Duration:</label>
                            <input
                                id="filterDays"
                                type="number"
                                className="filterDays"
                                // width={"20px"}
                                // maxLength="20px"
                                // value={durationDays}
                                // onChange={(e) => setDurationDays(e.target.value)}
                            />
                        </div>
                        <hr/>

                        <div className={"d-flex flex-row"}>

                            <label htmlFor="filterRate" className={"me-2 align-bottom"}>Rate:</label>
                            <input
                                id="filterRate"
                                type="number"
                                className="filterDays"
                                step={"0.5"}
                                min={0}
                                // width={"20px"}
                                // maxLength="20px"
                                // value={durationDays}
                                // onChange={(e) => setDurationDays(e.target.value)}
                            />
                        </div>
                        <hr/>

                        <b className="sidebar-parag-title">Sort by:</b>
                        <select className="form-select" id="sortby" aria-label="Default select example"
                            // onChange="sortPhones()"
                        >
                            <option value="0" key={0}>Longest</option>
                            <option value="-1" key={-1}>Shortest</option>
                            <option value="1" key={1}>Newer</option>
                            <option value="2" key={2}>Older</option>
                        </select>
                    </div>
                    <hr/>
                    </div>
                    <div className={"d-flex justify-content-around"}>

                    {/*<button className="sign-up-button mt-3 d-flex flex-row"*/}
                    {/*        // disabled={userRole != "STUDENT"} onClick={() => {handleEnroll()}}*/}
                    {/*>*/}
                    {/*    Enroll*/}
                    {/*</button>*/}
                        <Button variant="primary" type="submit" className="btn-outline-warning filter-submit w-75">
                            Filter
                        </Button>
                    </div>
                </aside>
                {isLoading ? <div>Loading...</div> :
                    <div className="flex flex-column w-100 justify-content-center">
                        <div className="cards-container d-flex flex-wrap mt-1">
                            {firstPage?.content.map((item, index) => (
                                <CourseComponent item={item} key={index}></CourseComponent>
                            ))}
                        </div>
                        <div className="pagination-container d-flex align-items-center flex-row">
                            <Pagination
                                className="flex flex-row pagination"
                                currentPage={currentPage}
                                totalPages={firstPage?.totalPages || 1}
                                onPageChange={onPageChange}
                                showIcons
                            />
                            <div className={"ms-3"}>
// todo: fix !
                                <input
                                    id="pageSizeInput"
                                    type="number"
                                    className="filterDays"
                                    step={"1"}
                                    min={1}
                                    max={25}
                                    placeholder={pageSize}
                                    // width={"20px"}
                                    // maxLength="20px"
                                    // value={durationDays}
                                    onChange={(e) => setPageSize(e.target.value)}
                                    title={"Items per page"}
                                />
                                {/*<label htmlFor="pageSizeInput" className={"ms-2 align-bottom"}>Items per page</label>*/}

                            </div>
                        </div>
                    </div>
                }
            </div>
            </div>
        </div>
    );
    }
;

export default CourseList;
