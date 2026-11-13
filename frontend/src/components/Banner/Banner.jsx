import './Banner.css'

export default function Banner({icon: Icon, children}) {
    return (
        <div className="banner">
            <Icon className="banner-icon"/>
            <p>{children}</p>
        </div>
    )
}