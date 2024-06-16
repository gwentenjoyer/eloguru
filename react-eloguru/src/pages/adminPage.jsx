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
                        Адміністрування користувачів
                    </NavLink>
                    <NavLink
                        to="teachersManagement"
                        className="pageSwitcherItem1 text-center"
                    >
                        Адміністрування вчителів
                    </NavLink>
                    <NavLink
                        to="cars"
                        className="pageSwitcherItem1 text-center"
                    >
                        Адміністрування транспорту
                    </NavLink>
                </div>
            </div>
        </div>
    );
}