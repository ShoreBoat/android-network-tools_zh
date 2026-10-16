import re, os, io, sys

SRC = r"C:/D/apk_hook/android-network-tools-zh/app/src/main/res/values/strings.xml"
OUT = r"C:/D/apk_hook/_zh"
os.makedirs(OUT, exist_ok=True)

lines = io.open(SRC, encoding="utf-8").read().split("\n")
# drop xml decl + <resources> open + </resources>
body = lines[2:]
assert body[-2].strip() == "</resources>", body[-3:]
body = body[:-2]

# group into blocks: block starts at a line that begins a comment or a resource
blocks = []
cur = []
for ln in body:
    s = ln.strip()
    if s.startswith("<!--") or s.startswith("<string") or s.startswith("<plurals") or s.startswith("<string-array") or s.startswith("<item") or s.startswith("<integer"):
        if cur:
            blocks.append(cur)
        cur = [ln]
    else:
        cur.append(ln)
if cur:
    blocks.append(cur)

def size(b):
    return sum(len(x) for x in b)

total = sum(size(b) for b in blocks)
N = 8
target = total / N
chunks, cur, csz = [], [], 0
for b in blocks:
    if csz > 0 and csz + size(b) > target * 1.15 and len(chunks) < N - 1:
        chunks.append(cur); cur, csz = [], 0
    cur.extend(b); csz += size(b)
chunks.append(cur)

for i, ch in enumerate(chunks, 1):
    path = os.path.join(OUT, "slice_%02d.xml" % i)
    with io.open(path, "w", encoding="utf-8") as f:
        f.write("<!-- SLICE %d -->\n" % i)
        f.write("\n".join(ch).rstrip("\n") + "\n")
    print("slice_%02d.xml  bytes=%d  resources=%d" % (i, size(ch), sum(1 for l in ch if re.match(r'\s*<(string|plurals|string-array)\b', l))))
print("TOTAL bytes=%d blocks=%d" % (total, len(blocks)))
