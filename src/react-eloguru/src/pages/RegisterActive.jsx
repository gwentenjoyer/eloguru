import React, { useState } from 'react';
import AdditionInfo from "../components/Login/AdditionInfo";
import "../css/RegisterActive.css"
export default function RegisterActive() {
    return (
        <div
            className={"py-3 bg-light w-100"}
        >
            <div
                className={"container w-50"}
            >
                <div
                    className={"d-flex flex-column g-5 m-4"}
                >
                    <div
                        className={"d-flex flex-column justify-content-center align-items-center mb-3"}
                    >
                        <h2 className={"text-center about-text mb-4"}>The final registration stage</h2>
                    </div>
                    <div
                        // className="modal-dialog modal-lg "
                    >
                        <div
                            className={"mb-3 d-flex flex-column justify-content-center"}
                        >
                            <div className={"border border-dark p-4 shadow p-3 mb-5 bg-white rounded"}>
                                <AdditionInfo/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    );
}
