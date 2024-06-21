import {useState, useEffect} from 'react';
import {Outlet} from 'react-router-dom';
// import getRefreshTokens.js from "./getRefreshTokens.js";

export default function RequireAuth({url}) {
    const [isOk, setIsOk] = useState(null);
    const [isFinal, setIsFinal] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch(`/${url}`,{credentials: 'include'});
            if (response.status === 200 && !response.redirected) {
                setIsOk(true);
            } else {
                setIsFinal(true);
            }
        };
        fetchData();
    }, [url]);

    if (isFinal) {
        window.location.href = "/?login=true";
        console.log("that's bad auth")
    }
    if (isOk === null) {
        return <div>Loading...</div>;
    }

    return isOk ? <Outlet/> : <div></div>;
}