import Link from "next/link";

export function Footer() {
  return (
    <footer className="border-t border-border/50 bg-gradient-to-b from-muted/50 to-muted/80">
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div>
            <h3 className="text-lg font-bold mb-4 bg-gradient-to-r from-primary to-purple-500 bg-clip-text text-transparent">E-Shop</h3>
            <p className="text-sm text-muted-foreground leading-relaxed">Modern headless e-commerce platform built with Next.js and Spring Boot.</p>
          </div>
          <div>
            <h4 className="font-semibold mb-4">Quick Links</h4>
            <ul className="space-y-3 text-sm text-muted-foreground">
              <li><Link href="/products" className="hover:text-foreground transition-colors duration-200 hover:translate-x-0.5 inline-block">Products</Link></li>
              <li><Link href="/cart" className="hover:text-foreground transition-colors duration-200 hover:translate-x-0.5 inline-block">Cart</Link></li>
              <li><Link href="/orders" className="hover:text-foreground transition-colors duration-200 hover:translate-x-0.5 inline-block">Orders</Link></li>
            </ul>
          </div>
          <div>
            <h4 className="font-semibold mb-4">Customer Service</h4>
            <ul className="space-y-3 text-sm text-muted-foreground">
              <li>Contact Us</li>
              <li>Shipping Policy</li>
              <li>Return Policy</li>
            </ul>
          </div>
        </div>
        <div className="mt-10 border-t border-border/50 pt-6 text-center text-sm text-muted-foreground">
          &copy; {new Date().getFullYear()} E-Shop. All rights reserved.
        </div>
      </div>
    </footer>
  );
}
