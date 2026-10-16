import io, os, re, sys
from xml.dom import minidom

ZH = r"C:/D/apk_hook/_zh"
PROJ = r"C:/D/apk_hook/android-network-tools-zh"
OUT = os.path.join(PROJ, "app/src/main/res/values-zh-rCN/strings.xml")

HEADER = ('<?xml version="1.0" encoding="utf-8"?>\n'
          '<!--\n'
          '    Simplified Chinese (zh-CN) translations.\n'
          '    Generated for the Net Swiss Knife 二开 (custom fork) build.\n'
          '\n'
          '    Maintenance: keep the resource names and the %%1$s / %%2$d placeholders\n'
          '    in sync with values/strings.xml — the build fails on a mismatch.\n'
          '-->\n'
          '<resources>\n')

parts = []
for i in range(1, 8):
    p = os.path.join(ZH, "zh_%02d.xml" % i)
    if not os.path.exists(p):
        print("MISSING", p); sys.exit(1)
    d = io.open(p, encoding="utf-8").read()
    # drop the "<!-- SLICE n -->" marker the splitter wrote
    d = re.sub(r"^\s*<!--\s*SLICE\s*\d+\s*-->\s*\n", "", d)
    parts.append(d.rstrip("\n"))

extra = os.path.join(ZH, "zh_extra.xml")
if os.path.exists(extra):
    parts.append(io.open(extra, encoding="utf-8").read().rstrip("\n"))

body = "\n\n".join(parts)
os.makedirs(os.path.dirname(OUT), exist_ok=True)
io.open(OUT, "w", encoding="utf-8", newline="\n").write(HEADER + body + "\n</resources>\n")

# ── validation ────────────────────────────────────────────────────────────────
def collect(path):
    doc = minidom.parse(path)
    out = []
    for res in doc.documentElement.childNodes:
        if res.nodeType != res.ELEMENT_NODE:
            continue
        texts = []
        for c in res.childNodes:
            if c.nodeType == c.ELEMENT_NODE:
                texts.append((c.getAttribute("quantity"), c.firstChild.data if c.firstChild else ""))
            elif c.nodeType == c.TEXT_NODE and c.data.strip():
                texts.append(("", c.data))
        out.append((res.tagName, res.getAttribute("name"), texts))
    return out

en = collect(os.path.join(PROJ, "app/src/main/res/values/strings.xml"))
zh = {n: (t, x) for t, n, x in collect(OUT)}
enames = set(n for _, n, _ in en)
errs = []
for tag, name, texts in en:
    if name not in zh:
        errs.append("MISSING %s" % name); continue
    ztag, ztexts = zh[name]
    if ztag != tag:
        errs.append("%s tag %s->%s" % (name, tag, ztag)); continue
    ph = re.compile(r"%\d+\$[sd]|%\d+\$[,.\d]*f|%[sd]")
    for (q1, t1), (q2, t2) in zip(texts, ztexts):
        if q1 != q2:
            errs.append("%s quantity %s->%s" % (name, q1, q2))
        if ph.findall(t1) != ph.findall(t2):
            errs.append("%s placeholders %s->%s" % (name, ph.findall(t1), ph.findall(t2)))
extra = set(zh) - enames
for n in sorted(extra):
    errs.append("EXTRA %s" % n)

print("en resources: %d   zh resources: %d" % (len(en), len(zh)))
print("output: %s (%d bytes)" % (OUT, os.path.getsize(OUT)))
if errs:
    print("FAIL: %d issue(s)" % len(errs))
    for e in errs[:40]:
        print("  ", e)
    sys.exit(2)
print("VALIDATION OK")
