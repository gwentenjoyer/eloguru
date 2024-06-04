import React, { useState } from 'react';

function Collapse({ label, children, id }) {
    const [isOpen, setIsOpen] = useState(false);

    const handleToggle = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div className="card">
            <div className="card-header" id={`heading${id}`}>
                <h5 className="mb-0">
                    <button
                        className="btn btn-link"
                        onClick={handleToggle}
                        aria-expanded={isOpen}
                        aria-controls={`collapse${id}`}
                    >
                        {label}
                    </button>
                </h5>
            </div>
            {isOpen && (
                <div id={`collapse${id}`} className="collapse show" aria-labelledby={`heading${id}`}>
                    <div className="card-body">
                        {children}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Collapse;
