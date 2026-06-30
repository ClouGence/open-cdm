#!/usr/bin/env python3
"""Generate a 1024x1024 PNG icon for CloudDM desktop app.

Usage:  python3 generate-icon.py [output.png] [--source favicon.ico]
"""
import sys
import struct
import zlib


def chunk(chunk_type, data):
    c = chunk_type + data
    crc = struct.pack('>I', zlib.crc32(c) & 0xffffffff)
    return struct.pack('>I', len(data)) + c + crc


def create_png_from_pixels(width, height, pixels):
    header = b'\x89PNG\r\n\x1a\n'
    ihdr = chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 2, 0, 0, 0))
    raw = b''
    for row in pixels:
        raw += b'\x00'
        for r, g, b in row:
            raw += struct.pack('BBB', r, g, b)
    idat = chunk(b'IDAT', zlib.compress(raw))
    iend = chunk(b'IEND', b'')
    return header + ihdr + idat + iend


def draw_rounded_rect(pixels, w, h, x0, y0, x1, y1, color, radius):
    for y in range(max(y0, 0), min(y1, h)):
        for x in range(max(x0, 0), min(x1, w)):
            in_corner = False
            if x < x0 + radius and y < y0 + radius:
                dx, dy = x - (x0 + radius), y - (y0 + radius)
                in_corner = dx * dx + dy * dy > radius * radius
            elif x >= x1 - radius and y < y0 + radius:
                dx, dy = x - (x1 - radius - 1), y - (y0 + radius)
                in_corner = dx * dx + dy * dy > radius * radius
            elif x < x0 + radius and y >= y1 - radius:
                dx, dy = x - (x0 + radius), y - (y1 - radius - 1)
                in_corner = dx * dx + dy * dy > radius * radius
            elif x >= x1 - radius and y >= y1 - radius:
                dx, dy = x - (x1 - radius - 1), y - (y1 - radius - 1)
                in_corner = dx * dx + dy * dy > radius * radius
            if not in_corner:
                pixels[y][x] = color


def generate_from_scratch(out_path):
    """Fallback: draw DM icon without external deps."""
    size = 1024
    bg = (24, 29, 38)
    fg = (255, 255, 255)
    accent = (34, 34, 34)

    pixels = [[bg for _ in range(size)] for _ in range(size)]

    margin = 120
    r = 160
    draw_rounded_rect(pixels, size, size, margin, margin, size - margin, size - margin, accent, r)

    char_y0 = 312
    stroke_w = 56

    # D (left half)
    D_x0 = 260
    D_w = 420
    D_h = 400
    for y in range(char_y0, char_y0 + D_h):
        for x in range(D_x0, D_x0 + stroke_w):
            if 0 <= x < size and 0 <= y < size:
                pixels[y][x] = fg
    for y in range(char_y0, char_y0 + stroke_w):
        for x in range(D_x0, D_x0 + D_w // 2 + stroke_w):
            if 0 <= x < size and 0 <= y < size:
                pixels[y][x] = fg
    for y in range(char_y0 + D_h - stroke_w, char_y0 + D_h):
        for x in range(D_x0, D_x0 + D_w // 2 + stroke_w):
            if 0 <= x < size and 0 <= y < size:
                pixels[y][x] = fg
    # D right curve
    cx, cy = D_x0 + D_w // 2, char_y0 + D_h // 2
    rx, ry = D_w // 2, D_h // 2 - stroke_w // 2
    for y in range(char_y0 + stroke_w, char_y0 + D_h - stroke_w):
        for x in range(D_x0 + D_w // 2 - stroke_w, D_x0 + D_w // 2 + stroke_w):
            if 0 <= x < size and 0 <= y < size:
                dx, dy = (x - cx) / (rx - stroke_w // 2), (y - cy) / ry
                if 0.7 <= dx * dx + dy * dy <= 1.2:
                    pixels[y][x] = fg

    # M (right half)
    M_x0 = D_x0 + D_w + 100
    M_w = 420
    M_h = 400
    for y in range(char_y0, char_y0 + M_h):
        for x in range(M_x0, M_x0 + stroke_w):
            if 0 <= x < size and 0 <= y < size:
                pixels[y][x] = fg
    for y in range(char_y0, char_y0 + M_h):
        for x in range(M_x0 + M_w - stroke_w, M_x0 + M_w):
            if 0 <= x < size and 0 <= y < size:
                pixels[y][x] = fg
    mid_x = M_x0 + M_w // 2
    for y in range(char_y0, char_y0 + M_h):
        frac = (y - char_y0) / M_h
        offset = int((M_w // 2 - stroke_w) * (1 - frac))
        for dx in range(stroke_w):
            for sx in [mid_x - offset + dx, mid_x + offset - stroke_w + dx]:
                if 0 <= sx < size and 0 <= y < size:
                    pixels[y][sx] = fg

    data = create_png_from_pixels(size, size, pixels)
    with open(out_path, 'wb') as f:
        f.write(data)
    print(f'Icon generated from scratch: {out_path} ({size}x{size})')


def generate_from_favicon(favicon_path, out_path):
    """Scale up favicon to 1024x1024, inverting alpha for macOS icon use.

    The favicon is a negative-space design: dark strokes with transparent cutouts
    for light browser tabs. We fill transparent pixels that are "inside" the icon
    with a light color so the DM letters are visible while corners stay dark.
    """
    from PIL import Image

    LIGHT_FILL = (230, 230, 235)
    DARK_BG = (24, 29, 38)

    ico = Image.open(favicon_path)
    ico = ico.convert('RGBA')
    w, h = ico.size
    print(f'Favicon source: {w}x{h}')

    pixels = ico.load()

    # Helper: is (x, y) bounded by opaque pixels in all 4 directions?
    def has_opaque_in_direction(x, y, dx, dy):
        cx, cy = x + dx, y + dy
        while 0 <= cx < w and 0 <= cy < h:
            if pixels[cx, cy][3] > 128:
                return True
            cx += dx
            cy += dy
        return False

    def is_inside_icon(x, y):
        return (has_opaque_in_direction(x, y, -1, 0) and
                has_opaque_in_direction(x, y, 1, 0) and
                has_opaque_in_direction(x, y, 0, -1) and
                has_opaque_in_direction(x, y, 0, 1))

    for y in range(h):
        for x in range(w):
            r, g, b, a = pixels[x, y]
            if a < 128:
                if is_inside_icon(x, y):
                    pixels[x, y] = LIGHT_FILL + (255,)
                else:
                    pixels[x, y] = DARK_BG + (255,)

    # Scale up to 1024x1024 preserving sharp pixel edges
    result = ico.resize((1024, 1024), Image.NEAREST)
    result = result.convert('RGB')

    result.save(out_path, 'PNG')
    print(f'Icon generated from favicon: {out_path} (1024x1024)')


def main():
    args = sys.argv[1:]
    out_path = 'icon.png'
    favicon_path = None

    i = 0
    while i < len(args):
        if args[i] == '--source' and i + 1 < len(args):
            favicon_path = args[i + 1]
            i += 2
        elif not args[i].startswith('--'):
            out_path = args[i]
            i += 1
        else:
            i += 1

    if favicon_path:
        generate_from_favicon(favicon_path, out_path)
    else:
        generate_from_scratch(out_path)


if __name__ == '__main__':
    main()
