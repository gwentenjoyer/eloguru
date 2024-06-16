const handleLogout = async () => {
    const response = await fetch(`${process.env.REACT_APP_BASE_URL}/accounts/logout`, { credentials: 'include'});
    if (!response.ok){
        console.error("Failed to fetch data user")
    }
    console.log(123)
    // navigate('/');
    // window.location.href = "/"

};
// TODO: DONT WORK
export default handleLogout();
