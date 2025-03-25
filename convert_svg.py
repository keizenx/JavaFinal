from PIL import Image, ImageDraw

def create_logo(filename, color, size=(100, 100)):
    image = Image.new('RGBA', size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    
    # Draw bowl shape
    draw.ellipse([20, 40, 80, 80], fill=color)
    draw.ellipse([25, 35, 75, 75], fill=(0, 0, 0, 0))
    
    # Draw chopsticks
    draw.line([35, 50, 40, 30], fill=color, width=3)
    draw.line([50, 50, 50, 25], fill=color, width=3)
    draw.line([65, 50, 60, 30], fill=color, width=3)
    
    image.save(f'src/main/resources/images/{filename}.png', 'PNG')

def create_social_icon(filename, size=(40, 40)):
    image = Image.new('RGBA', size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    
    # Background circle
    draw.ellipse([0, 0, size[0]-1, size[1]-1], fill='#B22222')
    
    # Icon specific shapes in white
    if filename == 'facebook':
        draw.rectangle([15, 10, 25, 30], fill='white')
        draw.rectangle([10, 15, 30, 20], fill='white')
    elif filename == 'twitter':
        points = [(10, 30), (20, 25), (25, 15), (30, 10)]
        draw.line(points, fill='white', width=3)
    elif filename == 'instagram':
        draw.rectangle([10, 10, 30, 30], fill='white', outline='white', width=2)
        draw.ellipse([15, 15, 25, 25], outline='#B22222', width=2)
    elif filename == 'map':
        draw.polygon([20, 10, 10, 20, 20, 30, 30, 20], fill='white')
        draw.ellipse([17, 17, 23, 23], fill='#B22222')
    
    image.save(f'src/main/resources/images/{filename}.png', 'PNG')

# Create logos
create_logo('logo', '#B22222')
create_logo('logo-white', 'white')

# Create social icons
for icon in ['facebook', 'twitter', 'instagram', 'map']:
    create_social_icon(icon)

print("All images have been created successfully!") 