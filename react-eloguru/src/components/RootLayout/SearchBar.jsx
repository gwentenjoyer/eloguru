import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../../css/searchbar.css"

function SearchBar() {
    const [searchInput, setSearchInput] = useState("");
    const navigate = useNavigate()

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        navigate(`/course${searchInput && `?label=${searchInput}`}`);
    }

    return (
        <div className="d-flex flex-row align-items-center ms-4 me-4 w-100">
            <form className="navbar-search w-100" onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="search"
                    placeholder="Search courses..."
                    // className={"w-100"}
                    onChange={handleChange}
                    value={searchInput}
                />
                <button type="submit"><i className="fa fa-search"></i></button>
            </form>
        </div>
    );
}

export default SearchBar;