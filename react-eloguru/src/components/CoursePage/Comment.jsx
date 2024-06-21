import React from "react";
import '../../css/Comment.css'
import Rating from "@mui/material/Rating";
function Comment({ name, text, rate}) {
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
        <div className="comment-item m-3 ">
            <div className="commname text-center text-middle align-items-center">
                <div>
                    <img src="/blankUser.png" alt="User photo" className="rounded-photo"/>
                </div>
                <div className="comment-center m-1">{name}</div>
            </div>
            <div className="rate-comment">
                <div className="rate d-flex align-items-center"><span className={"me-2"}>Rate:</span>
                    <Rating className={"mt-2 mb-2"}
                            name="simple-controlled"
                            value={rate}
                            readOnly
                            precision={0.5}
                    />
                </div>
                <div className="comment">{text}</div>
            </div>
        </div>
    );
}

export default Comment;