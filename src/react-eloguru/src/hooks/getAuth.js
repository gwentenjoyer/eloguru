import {useState, useEffect} from 'react';
import {Outlet} from 'react-router-dom';
// import getRefreshTokens from "./getRefreshTokens";

export default function RequireAuth({url}) {
    const [isOk, setIsOk] = useState(null);
    const [isFinal, setIsFinal] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch(`${process.env.REACT_BASE_URL}/${url}`,{credentials: 'include'});
            if (response.status === 200 && !response.redirected) {
                setIsOk(true);
            // } else if (await getRefreshTokens() === true) {
            //     await fetchData()
            } else {
                setIsFinal(true);
            }
        };
        fetchData();
    }, [url]);

    if (isFinal) {
        window.location.href = "login";
    }
    if (isOk === null) {
        return <div>Завантаження...</div>;
    }

    return isOk ? <Outlet/> : <div></div>;
}