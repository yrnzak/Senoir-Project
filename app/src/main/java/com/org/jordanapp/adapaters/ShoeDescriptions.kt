package com.org.jordanapp.adapaters

import kotlin.random.Random

class ShoeDescriptions {

    private val descriptions = listOf(
        "These Jordan 6 Retro shoes feature a sleek leather upper with a bold red and black colorway, giving a perfect mix of style and comfort. The cushioned sole ensures all-day wearability, while the iconic Jumpman logo showcases your love for basketball culture.",
        "Experience next-level comfort with these ultra-lightweight Jordan sneakers. Their mesh upper allows for maximum breathability, and the foam midsole offers responsive cushioning for every step. The color scheme blends neutral tones with pops of vibrant orange for a stylish and modern look.",
        "Inspired by classic basketball shoes, these Jordans combine high-performance design with a vintage aesthetic. The durable leather exterior complements the cushioned insole, providing stability on the court or street. A vibrant outsole adds a touch of flair to your every move.",
        "These sneakers offer a premium mix of materials, including suede and synthetic overlays. The cushioning technology is designed to absorb shock during high-intensity activities, while the stylish design ensures you stand out whether you're hitting the gym or strolling through the city.",
        "Built for agility and performance, these shoes feature a flexible upper with strategic support zones. The padded collar adds extra comfort, while the outsole offers superior grip on any surface. The bold color palette makes these shoes a must-have for any sneaker enthusiast.",
        "With a retro design and contemporary technology, these Jordan sneakers feature a high-top silhouette for ankle support. The durable rubber sole ensures traction on the court, and the bold design, complete with contrasting colors, is sure to turn heads wherever you go.",
        "Crafted for both casual and athletic wear, these shoes feature a breathable knit fabric that keeps your feet cool. The streamlined design, with its neutral tones and pops of neon green, pairs effortlessly with any outfit while providing the ultimate in foot support.",
        "These high-performance shoes are designed for athletes who value speed and agility. The lightweight material and responsive cushioning help you move quickly, while the sleek, minimalist design ensures you'll look great whether you're training or relaxing.",
        "Perfect for streetwear fans, these shoes combine a bold, chunky design with cutting-edge comfort. The padded insole provides all-day cushioning, while the futuristic color scheme, with its neon accents, brings a unique, eye-catching flair to any outfit.",
        "Blending modern aesthetics with classic comfort, these shoes feature a textured leather upper and a responsive foam midsole. The intricate detailing and clean lines make them a standout piece, while the supportive structure keeps you comfortable during long days on your feet.",
        "These Jordans are engineered for superior comfort and style. The plush, cushioned footbed offers cloud-like comfort, while the durable, grippy outsole makes them perfect for everyday wear. The bold design is ideal for those who want to stand out with their footwear.",
        "The sleek design of these shoes is complemented by a combination of synthetic and leather materials, ensuring durability and breathability. With a low-profile silhouette and lightweight construction, these sneakers are perfect for those who prefer a minimalist aesthetic without sacrificing comfort.",
        "These sneakers are crafted with a blend of suede and mesh, offering a premium look and feel. The responsive midsole cushions each step, and the bold color scheme adds a splash of personality to your outfit. Ideal for both casual and athletic activities.",
        "Designed for high-energy athletes, these shoes provide superior grip and responsiveness. The engineered upper offers flexibility and breathability, while the vibrant colorways add a stylish touch. Whether on the court or in the gym, these sneakers are built to keep up with your active lifestyle.",
        "These high-top sneakers are designed to offer ankle support and enhanced comfort, thanks to their cushioned collar and thick midsole. The durable leather upper is paired with a rubber outsole for maximum traction, making them perfect for both style and functionality.",
        "Combining style and performance, these shoes feature a mesh upper for breathability and a sturdy rubber sole for durability. The foam cushioning absorbs impact, ensuring a smooth and comfortable ride whether you're walking or running.",
        "These shoes feature a classic silhouette with modern detailing, including reflective elements for a standout look. The durable construction and padded insole make them ideal for daily wear, while the stylish design pairs easily with both casual and sporty outfits.",
        "Perfect for athletes, these sneakers offer a snug fit with their sock-like upper, providing both support and flexibility. The cushioned midsole delivers excellent shock absorption, and the sleek design with contrasting colors ensures you'll stand out whether you're training or hanging out.",
        "With a bold, eye-catching design, these shoes are built for both comfort and style. The cushioned insole provides excellent support, while the lightweight construction allows for all-day wear. The mix of synthetic and leather materials ensures durability and a premium feel.",
        "These Jordans are crafted for optimal performance, featuring a breathable upper and cushioned sole that absorbs impact. The streamlined design offers a sleek look, while the signature color scheme highlights their connection to basketball heritage and street style."
    )

    // Method to get a random description
    fun getRandomDescription(): String {
        return descriptions[Random.nextInt(descriptions.size)]
    }
}
