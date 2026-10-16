import re, sys, io, os
from xml.dom import minidom

ZH = r"C:/D/apk_hook/_zh"
NN = sys.argv[1].zfill(2)
src = os.path.join(ZH, "slice_%s.xml" % NN)
dst = os.path.join(ZH, "zh_%s.xml" % NN)

def load(p):
    d = io.open(p, encoding="utf-8").read()
    return d

def parse(d, p):
    try:
        return minidom.parseString("<resources>\n" + d + "\n</resources>")
    except Exception as e:
        print("XML_PARSE_FAIL %s: %s" % (p, e))
        sys.exit(1)

EN_TEXT = load(src)
ZH_TEXT = load(dst)
# check.py itself is one of the files; skip if missing
if not os.path.exists(dst):
    print("MISSING_OUTPUT", dst); sys.exit(1)

ed = parse(EN_TEXT, src)
zd = parse(ZH_TEXT, dst)

def collect(doc):
    out = []
    for res in doc.documentElement.childNodes:
        if res.nodeType != res.ELEMENT_NODE:
            continue
        name = res.getAttribute("name")
        texts = []
        for child in res.childNodes:
            if child.nodeType == child.ELEMENT_NODE:  # item
                texts.append((child.getAttribute("quantity"), child.firstChild.data if child.firstChild else ""))
            elif child.nodeType == child.TEXT_NODE and child.data.strip():
                texts.append(("", child.data))
        out.append((res.tagName, name, texts))
    return out

e, z = collect(ed), collect(zd)
errs, warns = [], []

if len(e) != len(z):
    errs.append("resource count differs: en=%d zh=%d" % (len(e), len(z)))

n = min(len(e), len(z))
for i in range(n):
    (et, en, etx), (zt, zn, ztx) = e[i], z[i]
    if en != zn:
        errs.append("#%d name mismatch: %s != %s" % (i, en, zn)); continue
    if et != zt:
        errs.append("%s: tag %s != %s" % (en, et, zt)); continue
    ph = re.compile(r"%\d+\$[sd]|%\d+\$[,.\d]*f|%[sd]")
    for (eq, ev), (zq, zv) in zip(etx, ztx):
        if eq != zq:
            errs.append("%s: quantity %s != %s" % (en, eq, zq)); continue
        a, b = ph.findall(ev), ph.findall(zv)
        if a != b:
            errs.append("%s: placeholders %s -> %s  [%s]" % (en, a, b, zv[:70]))
    for (_, zv) in ztx:
        if not zv.strip():
            continue
        cjk = len(re.findall(r"[\u4e00-\u9fff]", zv))
        ascii_words = re.findall(r"\b[A-Za-z]{2,}\b", zv)
        if cjk == 0 and len(ascii_words) >= 3:
            warns.append("%s: untranslated? %s" % (en, zv[:80]))
for name in [z[i][1] for i in range(n)]:
    pass
# check every zh name exists in en
enames = set(x[1] for x in e)
for _, zn, _ in z:
    if zn not in enames:
        errs.append("extra resource in zh: %s" % zn)

print("== %s: resources en=%d zh=%d ==" % (NN, len(e), len(z)))
for w in warns[:20]:
    print("WARN", w)
if errs:
    print("FAIL %d errors" % len(errs))
    for x in errs[:30]:
        print("  ERR", x)
    sys.exit(2)
print("OK  (%d warnings)" % len(warns))
