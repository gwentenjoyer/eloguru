import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function SearchBar() {
    const [searchInput, setSearchInput] = useState("");
    const navigate = useNavigate()

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        navigate(`/courses${searchInput && `?label=${searchInput}`}`);
    }

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="search"
                placeholder="Search here"
                onChange={handleChange}
                value={searchInput}
            />
            <button type="submit">Search</button>
        </form>
    );
}

export default SearchBar;
