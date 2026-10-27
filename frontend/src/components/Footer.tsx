const Footer = () => {
  const currentYear: number = new Date().getFullYear()

  return (
    <footer className="bg-aegean-dark text-white">
      <div className="container mx-auto py-8 text-center">
        &copy; {currentYear} Athens Tours. All rights reserved.
      </div>
    </footer>
  )
}
export default Footer;
