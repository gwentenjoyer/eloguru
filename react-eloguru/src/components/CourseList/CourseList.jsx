import React, { useEffect, useState } from 'react';
import '../../css/CourseList.css';
import CourseComponent from "./CourseComponent";
import { Pagination } from "flowbite-react";
import {useLocation} from "react-router-dom";
import Button from "react-bootstrap/Button";

const CourseList = () => {
    const defaultPageSize = 3;
    const [firstPage, setFirstPage] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize, setPageSize] = useState(defaultPageSize);


    const [sortOrder, setSortOrder] = useState("asc");
    const [sortBy, setSortBy] = useState("longest");
    const [filterDuration, setFilterDuration] = useState("");
    const [filterRate, setFilterRate] = useState("");

    const location  = useLocation();


    const handleSortOrderChange = (event) => {
        setSortOrder(event.target.value);
        fetchUserInfo(event.target.value, sortBy, filterDuration, filterRate);
    };

    const handleSortByChange = (event) => {
        setSortBy(event.target.value);
        fetchUserInfo(sortOrder, event.target.value, filterDuration, filterRate);
    };

    const handleFilterDurationChange = (event) => {
        setFilterDuration(event.target.value);
    };

    const handleFilterRateChange = (event) => {
        setFilterRate(event.target.value);
    };

    const handleFilterSubmit = () => {
        fetchUserInfo(sortOrder, sortBy, filterDuration, filterRate);
    };


    const fetchUserInfo = async (page, size, order, sortBy, duration, rate) => {
        try {

            const queryParams = new URLSearchParams(location.search);
            const label = queryParams.get('label');
            console.log({order, duration, rate})
            // console.log(`${process.env.REACT_APP_BASE_URL}/courses?page=${page}&size=${size}${label ? `&label=${label}`: ""}`);
            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses?page=${page}&size=${size}${label ? `&label=${label}` : ""}&sortBy=${sortBy}&order=${order}${duration ? `&duration=${duration}` : ""}${rate ? `&rate=${rate}` : ""}`, { credentials: 'include' });
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
                            <b>Filter courses by criteria:</b>
                            <hr/>

                            <div className="d-flex flex-row">
                                <label htmlFor="filterDays" className="me-2 align-bottom">Duration:</label>
                                <input
                                    id="filterDays"
                                    type="number"
                                    className="filterDays"
                                    value={filterDuration}
                                    onChange={handleFilterDurationChange}
                                />
                            </div>
                            <hr/>

                            <div className="d-flex flex-row">
                                <label htmlFor="filterRate" className="me-2 align-bottom">Rate:</label>
                                <input
                                    id="filterRate"
                                    type="number"
                                    className="filterDays"
                                    step="0.5"
                                    min={0}
                                    value={filterRate}
                                    onChange={handleFilterRateChange}
                                />
                            </div>
                            <hr/>

                            <div>
                                <b className="sidebar-parag-title">Sort by:</b>
                                <select className="form-select" id="sortOrder" value={sortOrder}
                                        onChange={handleSortOrderChange} aria-label="Sort Order">
                                    <option value="asc">Ascending</option>
                                    <option value="desc">Descending</option>
                                </select>
                                <select className="form-select" id="sortBy" value={sortBy} onChange={handleSortByChange}
                                        aria-label="Sort By">
                                    <option value="longest">Longest</option>
                                    <option value="shortest">Shortest</option>
                                    <option value="newer">Newer</option>
                                    <option value="older">Older</option>
                                </select>
                            </div>
                            <hr/>

                            <div className="d-flex justify-content-around">
                                <button
                                    className="btn btn-outline-warning filter-submit w-75"
                                    onClick={handleFilterSubmit}
                                >
                                    Filter
                                </button>
                            </div>
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
                                    {/*// todo: fix !*/}
                                    <input
                                        id="pageSizeInput"
                                        type="number"
                                        className="filterDays"
                                        step={"1"}
                                        min={1}
                                        max={25}
                                        placeholder={"Size"}
                                        // width={"20px"}
                                        // maxLength="20px"
                                        // value={durationDays}
                                        onChange={(e) => setPageSize(e.target.value || defaultPageSize)}
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
