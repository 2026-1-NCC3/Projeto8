import React, { useRef, useEffect, ReactNode } from "react";

interface GradientDivProps {
    children: ReactNode;
    className?: string;
}

const Gradient: React.FC<GradientDivProps> = ({ children, className = "" }) => {
    const divRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        let animationFrameId: number;

        // 1. Set up our physics variables
        // Default to the center of the screen initially
        let currentX = window.innerWidth / 2;
        let currentY = window.innerHeight / 2;
        let targetX = window.innerWidth / 2;
        let targetY = window.innerHeight / 2;
        let velX = 0;
        let velY = 0;

        // 2. Tweak these to change the "feel" of the bounce
        const tension = 0.05;  // How strongly it's pulled toward the mouse
        const friction = 0.85; // How much it slows down (lower = bouncier, higher = sluggish)

        // 3. The animation loop
        const animate = () => {
            // Calculate distance between current position and the mouse (target)
            const dx = targetX - currentX;
            const dy = targetY - currentY;

            // Apply spring force (tension) to velocity
            velX += dx * tension;
            velY += dy * tension;

            // Apply friction so it eventually stops bouncing
            velX *= friction;
            velY *= friction;

            // Move the current position
            currentX += velX;
            currentY += velY;

            // Apply to the DOM
            if (divRef.current) {
                divRef.current.style.setProperty('--mouse-x', `${currentX}px`);
                divRef.current.style.setProperty('--mouse-y', `${currentY}px`);
            }

            // Keep the loop running
            animationFrameId = requestAnimationFrame(animate);
        };

        const handleMouseMove = (ev: MouseEvent) => {
            // Only update the *target* on mouse move
            targetX = ev.clientX;
            targetY = ev.clientY;
        };

        window.addEventListener('mousemove', handleMouseMove);

        // Start the physics loop
        animate();

        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
            cancelAnimationFrame(animationFrameId);
        };
    }, []);

    return (
        <div
            ref={divRef}
            className={className}
            style={{
                backgroundImage: `radial-gradient(circle at var(--mouse-x, 60%) var(--mouse-y, 60%), #74c9d6, #01afc8 60%)`
            }}
        >
            {children}
        </div>
    );
};

export default Gradient;