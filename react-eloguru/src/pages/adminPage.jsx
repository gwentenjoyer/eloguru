import React from "react";
import {NavLink} from "react-router-dom";

export default function AdminPage() {
    return (
        <div className="container blank1 my-5">
            <div className="appForm1">
                <div className="pageSwitcher1 justify-content-center">
                    <NavLink
                        to="data"
                        className="pageSwitcherItem1 text-center"
                    >
                        Administrate users
                    </NavLink>
                    <NavLink
                        to="course"
                        className="pageSwitcherItem1 text-center"
                    >
                        Administrate courses
                    </NavLink>
                </div>
            </div>
        </div>
    );
}