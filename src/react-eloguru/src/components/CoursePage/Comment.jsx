import React from "react";
import '../../css/Comment.css'
function Comment({ name, text }) {
    return (
        // <div className="css-l6lvqi">
        //     <div className="css-wuhn9k">
        //         <div className="cds-119 cds-Typography-base css-1oqze1z cds-121 text-center" aria-hidden="true">{name}</div>
        //     </div>
        //     <div className="css-12sixi4">
        //         <div className="css-1if3bys">
        //             <div className="rc-TogglableContent about-section collapsed">
        //                 <div className="content">
        //                     <div className="content-inner">
        //                         <p>{text}</p>
        //                     </div>
        //                 </div>
        //             </div>
        //         </div>
        //     </div>
        // </div>
        <div className="comment-item">
            <div className="name text-center text-middle align-items-center">
                <div>Name: John Doe</div>
            </div>
            <div className="rate-comment">
                <div className="rate">Rate: 5/5</div>
                <div className="comment">Comment: Great service!</div>
            </div>
        </div>
    );
}

export default Comment;