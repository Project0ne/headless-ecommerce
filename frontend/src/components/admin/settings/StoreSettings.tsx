"use client";

import { useState, useEffect } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Switch } from "@/components/ui/switch";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import {
  Store,
  CreditCard,
  Truck,
  Percent,
  Coins,
  Globe,
  ShoppingCart,
  Shield,
  Users,
  FileText,
  Image,
  Bell,
  DollarSign,
  History,
  Settings,
  Plus,
  Edit,
  Trash2,
  Upload,
} from "lucide-react";
import settingsService, {
  StoreConfig,
  PaymentMethod,
  ShippingMethod,
  TaxRate,
  Currency,
  Policy,
  Staff,
  AuditLog,
  Media,
} from "@/services/settings-service";

// ============ Store Basic Settings ============
export function StoreBasicSettings() {
  const { toast } = useToast();
  const [config, setConfig] = useState<StoreConfig>({
    storeName: "",
    timezone: "Asia/Shanghai",
    currencyCode: "CNY",
    currencySymbol: "¥",
    language: "zh-CN",
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    settingsService.getStoreConfig().then((res) => {
      setConfig(res.data);
      setLoading(false);
    });
  }, []);

  const handleSave = async () => {
    await settingsService.updateStoreConfig(config);
    toast({ title: "保存成功", description: "店铺基本信息已更新" });
  };

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Store className="h-5 w-5" />
          基本信息
        </CardTitle>
        <CardDescription>设置您的店铺名称、Logo、时区、联系地址等信息</CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="grid gap-4 md:grid-cols-2">
          <div className="space-y-2">
            <Label>店铺名称</Label>
            <Input
              value={config.storeName}
              onChange={(e) => setConfig({ ...config, storeName: e.target.value })}
              placeholder="我的商店"
            />
          </div>
          <div className="space-y-2">
            <Label>店铺 Logo URL</Label>
            <Input
              value={config.storeLogo || ""}
              onChange={(e) => setConfig({ ...config, storeLogo: e.target.value })}
              placeholder="https://example.com/logo.png"
            />
          </div>
        </div>

        <div className="space-y-2">
          <Label>店铺描述</Label>
          <Textarea
            value={config.storeDescription || ""}
            onChange={(e) => setConfig({ ...config, storeDescription: e.target.value })}
            placeholder="关于您的店铺..."
            rows={3}
          />
        </div>

        <div className="grid gap-4 md:grid-cols-2">
          <div className="space-y-2">
            <Label>联系邮箱</Label>
            <Input
              type="email"
              value={config.contactEmail || ""}
              onChange={(e) => setConfig({ ...config, contactEmail: e.target.value })}
              placeholder="contact@example.com"
            />
          </div>
          <div className="space-y-2">
            <Label>联系电话</Label>
            <Input
              value={config.contactPhone || ""}
              onChange={(e) => setConfig({ ...config, contactPhone: e.target.value })}
              placeholder="+86 138-xxxx-xxxx"
            />
          </div>
        </div>

        <div className="space-y-2">
          <Label>联系地址</Label>
          <Input
            value={config.contactAddress || ""}
            onChange={(e) => setConfig({ ...config, contactAddress: e.target.value })}
            placeholder="详细地址"
          />
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          <div className="space-y-2">
            <Label>时区</Label>
            <Select value={config.timezone} onValueChange={(v) => setConfig({ ...config, timezone: v })}>
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="Asia/Shanghai">Asia/Shanghai (UTC+8)</SelectItem>
                <SelectItem value="Asia/Tokyo">Asia/Tokyo (UTC+9)</SelectItem>
                <SelectItem value="America/New_York">America/New_York (UTC-5)</SelectItem>
                <SelectItem value="Europe/London">Europe/London (UTC+0)</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div className="space-y-2">
            <Label>默认货币</Label>
            <Select value={config.currencyCode} onValueChange={(v) => setConfig({ ...config, currencyCode: v })}>
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="CNY">CNY (¥ 人民币)</SelectItem>
                <SelectItem value="USD">USD ($ 美元)</SelectItem>
                <SelectItem value="EUR">EUR (€ 欧元)</SelectItem>
                <SelectItem value="GBP">GBP (£ 英镑)</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div className="space-y-2">
            <Label>语言</Label>
            <Select value={config.language} onValueChange={(v) => setConfig({ ...config, language: v })}>
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="zh-CN">简体中文</SelectItem>
                <SelectItem value="zh-TW">繁體中文</SelectItem>
                <SelectItem value="en-US">English</SelectItem>
                <SelectItem value="ja-JP">日本語</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        <div className="space-y-2">
          <Label>自定义域名</Label>
          <Input
            value={config.customDomain || ""}
            onChange={(e) => setConfig({ ...config, customDomain: e.target.value })}
            placeholder="shop.example.com"
          />
        </div>

        <div className="space-y-2">
          <Label>页脚文本</Label>
          <Textarea
            value={config.footerText || ""}
            onChange={(e) => setConfig({ ...config, footerText: e.target.value })}
            placeholder="© 2024 My Store. All rights reserved."
            rows={2}
          />
        </div>

        <Button onClick={handleSave} className="bg-gradient-to-r from-primary to-blue-600">
          保存更改
        </Button>
      </CardContent>
    </Card>
  );
}

// ============ Payment Methods Settings ============
export function PaymentSettings() {
  const { toast } = useToast();
  const [methods, setMethods] = useState<PaymentMethod[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingMethod, setEditingMethod] = useState<PaymentMethod | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  const loadMethods = () => {
    settingsService.getPaymentMethods().then((res) => {
      setMethods(res.data);
      setLoading(false);
    });
  };

  useEffect(() => { loadMethods(); }, []);

  const handleSave = async () => {
    if (!editingMethod) return;
    if (editingMethod.id) {
      await settingsService.updatePaymentMethod(editingMethod.id, editingMethod);
    } else {
      await settingsService.createPaymentMethod(editingMethod);
    }
    toast({ title: "保存成功" });
    setDialogOpen(false);
    loadMethods();
  };

  const handleToggle = async (method: PaymentMethod) => {
    await settingsService.updatePaymentMethod(method.id!, { ...method, isEnabled: !method.isEnabled });
    loadMethods();
  };

  const handleDelete = async (id: number) => {
    await settingsService.deletePaymentMethod(id);
    toast({ title: "已删除" });
    loadMethods();
  };

  const paymentTypes = [
    { value: "CREDIT_CARD", label: "信用卡" },
    { value: "DEBIT_CARD", label: "借记卡" },
    { value: "PAYPAL", label: "PayPal" },
    { value: "WECHAT_PAY", label: "微信支付" },
    { value: "ALIPAY", label: "支付宝" },
    { value: "BANK_TRANSFER", label: "银行转账" },
    { value: "CASH_ON_DELIVERY", label: "货到付款" },
    { value: "STRIPE", label: "Stripe" },
    { value: "APPLE_PAY", label: "Apple Pay" },
    { value: "GOOGLE_PAY", label: "Google Pay" },
  ];

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle className="flex items-center gap-2">
            <CreditCard className="h-5 w-5" />
            收款方式
          </CardTitle>
          <CardDescription>设置您的收款方式、PayPal、信用卡等信息</CardDescription>
        </div>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button size="sm" onClick={() => setEditingMethod({ name: "", type: "CREDIT_CARD", isEnabled: true, sortOrder: 0 })}>
              <Plus className="h-4 w-4 mr-1" /> 添加
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>{editingMethod?.id ? "编辑" : "添加"}收款方式</DialogTitle>
            </DialogHeader>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label>名称</Label>
                <Input value={editingMethod?.name || ""} onChange={(e) => setEditingMethod({ ...editingMethod!, name: e.target.value })} />
              </div>
              <div className="space-y-2">
                <Label>类型</Label>
                <Select value={editingMethod?.type} onValueChange={(v) => setEditingMethod({ ...editingMethod!, type: v })}>
                  <SelectTrigger><SelectValue /></SelectTrigger>
                  <SelectContent>
                    {paymentTypes.map((t) => <SelectItem key={t.value} value={t.value}>{t.label}</SelectItem>)}
                  </SelectContent>
                </Select>
              </div>
              <div className="flex items-center justify-between">
                <Label>启用</Label>
                <Switch checked={editingMethod?.isEnabled} onCheckedChange={(v) => setEditingMethod({ ...editingMethod!, isEnabled: v })} />
              </div>
            </div>
            <DialogFooter>
              <Button onClick={handleSave}>保存</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>名称</TableHead>
              <TableHead>类型</TableHead>
              <TableHead>状态</TableHead>
              <TableHead className="text-right">操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {methods.map((m) => (
              <TableRow key={m.id}>
                <TableCell className="font-medium">{m.name}</TableCell>
                <TableCell><Badge variant="outline">{m.type}</Badge></TableCell>
                <TableCell>
                  <Switch checked={m.isEnabled} onCheckedChange={() => handleToggle(m)} />
                </TableCell>
                <TableCell className="text-right">
                  <Button variant="ghost" size="icon" onClick={() => { setEditingMethod(m); setDialogOpen(true); }}>
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" size="icon" onClick={() => handleDelete(m.id!)}>
                    <Trash2 className="h-4 w-4 text-destructive" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

// ============ Shipping Settings ============
export function ShippingSettings() {
  const { toast } = useToast();
  const [methods, setMethods] = useState<ShippingMethod[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingMethod, setEditingMethod] = useState<ShippingMethod | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  const loadMethods = () => {
    settingsService.getShippingMethods().then((res) => {
      setMethods(res.data);
      setLoading(false);
    });
  };

  useEffect(() => { loadMethods(); }, []);

  const handleSave = async () => {
    if (!editingMethod) return;
    if (editingMethod.id) {
      await settingsService.updateShippingMethod(editingMethod.id, editingMethod);
    } else {
      await settingsService.createShippingMethod(editingMethod);
    }
    toast({ title: "保存成功" });
    setDialogOpen(false);
    loadMethods();
  };

  const handleDelete = async (id: number) => {
    await settingsService.deleteShippingMethod(id);
    toast({ title: "已删除" });
    loadMethods();
  };

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle className="flex items-center gap-2">
            <Truck className="h-5 w-5" />
            物流配送
          </CardTitle>
          <CardDescription>设置您的配送商品的物流方式</CardDescription>
        </div>
        <Button size="sm" onClick={() => { setEditingMethod({ name: "", baseFee: 0, isEnabled: true, sortOrder: 0 }); setDialogOpen(true); }}>
          <Plus className="h-4 w-4 mr-1" /> 添加
        </Button>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>名称</TableHead>
              <TableHead>基础运费</TableHead>
              <TableHead>包邮门槛</TableHead>
              <TableHead>预计天数</TableHead>
              <TableHead>状态</TableHead>
              <TableHead className="text-right">操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {methods.map((m) => (
              <TableRow key={m.id}>
                <TableCell className="font-medium">{m.name}</TableCell>
                <TableCell>¥{m.baseFee.toFixed(2)}</TableCell>
                <TableCell>{m.freeShippingThreshold ? `¥${m.freeShippingThreshold}` : "-"}</TableCell>
                <TableCell>{m.estimatedDaysMin}-{m.estimatedDaysMax}天</TableCell>
                <TableCell><Badge variant={m.isEnabled ? "default" : "secondary"}>{m.isEnabled ? "启用" : "禁用"}</Badge></TableCell>
                <TableCell className="text-right">
                  <Button variant="ghost" size="icon" onClick={() => { setEditingMethod(m); setDialogOpen(true); }}>
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" size="icon" onClick={() => handleDelete(m.id!)}>
                    <Trash2 className="h-4 w-4 text-destructive" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>

        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogContent>
            <DialogHeader><DialogTitle>{editingMethod?.id ? "编辑" : "添加"}物流方式</DialogTitle></DialogHeader>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label>名称</Label>
                <Input value={editingMethod?.name || ""} onChange={(e) => setEditingMethod({ ...editingMethod!, name: e.target.value })} />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>基础运费 (¥)</Label>
                  <Input type="number" value={editingMethod?.baseFee || 0} onChange={(e) => setEditingMethod({ ...editingMethod!, baseFee: parseFloat(e.target.value) })} />
                </div>
                <div className="space-y-2">
                  <Label>包邮门槛 (¥)</Label>
                  <Input type="number" value={editingMethod?.freeShippingThreshold || ""} onChange={(e) => setEditingMethod({ ...editingMethod!, freeShippingThreshold: parseFloat(e.target.value) || undefined })} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>最少天数</Label>
                  <Input type="number" value={editingMethod?.estimatedDaysMin || ""} onChange={(e) => setEditingMethod({ ...editingMethod!, estimatedDaysMin: parseInt(e.target.value) || undefined })} />
                </div>
                <div className="space-y-2">
                  <Label>最多天数</Label>
                  <Input type="number" value={editingMethod?.estimatedDaysMax || ""} onChange={(e) => setEditingMethod({ ...editingMethod!, estimatedDaysMax: parseInt(e.target.value) || undefined })} />
                </div>
              </div>
            </div>
            <DialogFooter><Button onClick={handleSave}>保存</Button></DialogFooter>
          </DialogContent>
        </Dialog>
      </CardContent>
    </Card>
  );
}

// ============ Tax Settings ============
export function TaxSettings() {
  const { toast } = useToast();
  const [rates, setRates] = useState<TaxRate[]>([]);
  const [loading, setLoading] = useState(true);

  const loadRates = () => {
    settingsService.getTaxRates().then((res) => {
      setRates(res.data);
      setLoading(false);
    });
  };

  useEffect(() => { loadRates(); }, []);

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Percent className="h-5 w-5" />
          税费
        </CardTitle>
        <CardDescription>根据不同的国家地区设置购物税费</CardDescription>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>名称</TableHead>
              <TableHead>国家代码</TableHead>
              <TableHead>税率</TableHead>
              <TableHead>状态</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {rates.map((r) => (
              <TableRow key={r.id}>
                <TableCell className="font-medium">{r.name}</TableCell>
                <TableCell><Badge variant="outline">{r.countryCode}</Badge></TableCell>
                <TableCell>{r.taxRate}%</TableCell>
                <TableCell><Badge variant={r.isEnabled ? "default" : "secondary"}>{r.isEnabled ? "启用" : "禁用"}</Badge></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

// ============ Currency Settings ============
export function CurrencySettings() {
  const { toast } = useToast();
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const [loading, setLoading] = useState(true);

  const loadCurrencies = () => {
    settingsService.getCurrencies().then((res) => {
      setCurrencies(res.data);
      setLoading(false);
    });
  };

  useEffect(() => { loadCurrencies(); }, []);

  const handleSetDefault = async (id: number) => {
    await settingsService.setDefaultCurrency(id);
    toast({ title: "已设为默认货币" });
    loadCurrencies();
  };

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Coins className="h-5 w-5" />
          货币
        </CardTitle>
        <CardDescription>给店铺设置多种展示货币，方便不同国家/地区的顾客查看商品</CardDescription>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>代码</TableHead>
              <TableHead>名称</TableHead>
              <TableHead>符号</TableHead>
              <TableHead>汇率</TableHead>
              <TableHead>默认</TableHead>
              <TableHead>状态</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {currencies.map((c) => (
              <TableRow key={c.id}>
                <TableCell><Badge variant="outline">{c.code}</Badge></TableCell>
                <TableCell className="font-medium">{c.name}</TableCell>
                <TableCell>{c.symbol}</TableCell>
                <TableCell>{c.exchangeRate}</TableCell>
                <TableCell>
                  {c.isDefault ? (
                    <Badge>默认</Badge>
                  ) : (
                    <Button variant="ghost" size="sm" onClick={() => handleSetDefault(c.id!)}>设为默认</Button>
                  )}
                </TableCell>
                <TableCell><Badge variant={c.isEnabled ? "default" : "secondary"}>{c.isEnabled ? "启用" : "禁用"}</Badge></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

// ============ Domain Settings ============
export function DomainSettings() {
  const [domain, setDomain] = useState("");

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Globe className="h-5 w-5" />
          设置域名
        </CardTitle>
        <CardDescription>绑定商户自己注册域名，提升品牌形象</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="space-y-2">
          <Label>自定义域名</Label>
          <Input value={domain} onChange={(e) => setDomain(e.target.value)} placeholder="shop.example.com" />
          <p className="text-sm text-muted-foreground">请将域名 CNAME 指向 shop.example.com</p>
        </div>
        <Button>绑定域名</Button>
      </CardContent>
    </Card>
  );
}

// ============ Language Settings ============
export function LanguageSettings() {
  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Globe className="h-5 w-5" />
          店铺语言
        </CardTitle>
        <CardDescription>管理店铺后台和顾客可使用的多国语言</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="space-y-2">
          <Label>默认语言</Label>
          <Select defaultValue="zh-CN">
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="zh-CN">简体中文</SelectItem>
              <SelectItem value="zh-TW">繁體中文</SelectItem>
              <SelectItem value="en-US">English</SelectItem>
              <SelectItem value="ja-JP">日本語</SelectItem>
              <SelectItem value="ko-KR">한국어</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          <Label>启用的语言</Label>
          <div className="flex flex-wrap gap-2">
            <Badge>简体中文</Badge>
            <Badge variant="outline">English</Badge>
            <Badge variant="outline">日本語</Badge>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

// ============ Checkout Settings ============
export function CheckoutSettings() {
  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <ShoppingCart className="h-5 w-5" />
          结账
        </CardTitle>
        <CardDescription>管理店铺结账相关流程</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <Label>允许游客结账</Label>
            <p className="text-sm text-muted-foreground">无需登录即可购买</p>
          </div>
          <Switch defaultChecked />
        </div>
        <div className="flex items-center justify-between">
          <div>
            <Label>要求电话号码</Label>
            <p className="text-sm text-muted-foreground">结账时必填</p>
          </div>
          <Switch />
        </div>
        <div className="flex items-center justify-between">
          <div>
            <Label>订单确认邮件</Label>
            <p className="text-sm text-muted-foreground">下单后自动发送</p>
          </div>
          <Switch defaultChecked />
        </div>
      </CardContent>
    </Card>
  );
}

// ============ Policy Settings ============
export function PolicySettings() {
  const [policies, setPolicies] = useState<Policy[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    settingsService.getPolicies().then((res) => {
      setPolicies(res.data);
      setLoading(false);
    });
  }, []);

  const policyTypes = [
    { type: "PRIVACY", label: "隐私政策", icon: Shield },
    { type: "REFUND", label: "退款政策", icon: FileText },
    { type: "TERMS", label: "服务条款", icon: FileText },
    { type: "SHIPPING", label: "配送政策", icon: Truck },
  ];

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Shield className="h-5 w-5" />
          政策条款
        </CardTitle>
        <CardDescription>设置您的店铺政策、条款和协议</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        {policies.map((p) => (
          <div key={p.id} className="p-4 border rounded-lg">
            <div className="flex items-center justify-between mb-2">
              <h4 className="font-medium">{p.title}</h4>
              <div className="flex items-center gap-2">
                {p.isRequired && <Badge variant="secondary">必填</Badge>}
                <Switch checked={p.isEnabled} />
              </div>
            </div>
            <Textarea defaultValue={p.content} rows={4} placeholder={`${p.title}内容...`} />
          </div>
        ))}
        <Button>保存所有政策</Button>
      </CardContent>
    </Card>
  );
}

// ============ Staff Settings ============
export function StaffSettings() {
  const { toast } = useToast();
  const [staff, setStaff] = useState<Staff[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    settingsService.getStaffList().then((res) => {
      setStaff(res.data);
      setLoading(false);
    });
  }, []);

  const roles = [
    { value: "SUPER_ADMIN", label: "超级管理员", color: "bg-red-100 text-red-800" },
    { value: "ADMIN", label: "管理员", color: "bg-orange-100 text-orange-800" },
    { value: "MANAGER", label: "经理", color: "bg-blue-100 text-blue-800" },
    { value: "EDITOR", label: "编辑", color: "bg-green-100 text-green-800" },
    { value: "VIEWER", label: "访客", color: "bg-gray-100 text-gray-800" },
  ];

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle className="flex items-center gap-2">
            <Users className="h-5 w-5" />
            员工账号
          </CardTitle>
          <CardDescription>设置员工账号和访问权限</CardDescription>
        </div>
        <Button size="sm"><Plus className="h-4 w-4 mr-1" /> 添加员工</Button>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>用户名</TableHead>
              <TableHead>显示名</TableHead>
              <TableHead>角色</TableHead>
              <TableHead>状态</TableHead>
              <TableHead>最后登录</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {staff.map((s) => (
              <TableRow key={s.id}>
                <TableCell className="font-medium">{s.username}</TableCell>
                <TableCell>{s.displayName}</TableCell>
                <TableCell><Badge className={roles.find(r => r.value === s.role)?.color}>{roles.find(r => r.value === s.role)?.label}</Badge></TableCell>
                <TableCell><Badge variant={s.isActive ? "default" : "secondary"}>{s.isActive ? "活跃" : "禁用"}</Badge></TableCell>
                <TableCell className="text-muted-foreground">{s.lastLoginAt || "从未登录"}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

// ============ Audit Log ============
export function AuditLogSettings() {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    settingsService.getAuditLogs().then((res) => {
      setLogs(res.data.content);
      setLoading(false);
    });
  }, []);

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <History className="h-5 w-5" />
          操作日志
        </CardTitle>
        <CardDescription>展示员工在店铺操作的日志记录</CardDescription>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>时间</TableHead>
              <TableHead>操作人</TableHead>
              <TableHead>操作</TableHead>
              <TableHead>对象</TableHead>
              <TableHead>IP</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {logs.map((log) => (
              <TableRow key={log.id}>
                <TableCell className="text-muted-foreground">{new Date(log.createdAt).toLocaleString()}</TableCell>
                <TableCell>{log.operatorName}</TableCell>
                <TableCell><Badge variant="outline">{log.action}</Badge></TableCell>
                <TableCell>{log.entityName || "-"}</TableCell>
                <TableCell className="text-muted-foreground">{log.ipAddress || "-"}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

// ============ Media Library ============
export function MediaLibrary() {
  const [media, setMedia] = useState<Media[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    settingsService.getMediaList().then((res) => {
      setMedia(res.data.content);
      setLoading(false);
    });
  }, []);

  const formatSize = (bytes: number) => {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
    return (bytes / (1024 * 1024)).toFixed(1) + " MB";
  };

  if (loading) return <div className="p-6">加载中...</div>;

  return (
    <Card className="shadow-card">
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle className="flex items-center gap-2">
            <Image className="h-5 w-5" />
            素材库
          </CardTitle>
          <CardDescription>管理上传的所有文件和图片，方便二次使用和查找</CardDescription>
        </div>
        <Button size="sm"><Upload className="h-4 w-4 mr-1" /> 上传</Button>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
          {media.map((m) => (
            <div key={m.id} className="aspect-square border rounded-lg overflow-hidden bg-muted flex items-center justify-center">
              {m.mimeType.startsWith("image/") ? (
                <img src={m.fileUrl} alt={m.originalName} className="w-full h-full object-cover" />
              ) : (
                <FileText className="h-8 w-8 text-muted-foreground" />
              )}
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}

// ============ Notification Settings ============
export function NotificationSettings() {
  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Bell className="h-5 w-5" />
          消息通知
        </CardTitle>
        <CardDescription>管理店铺推送给客户的邮件通知模板</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        {[
          { name: "订单确认", desc: "客户下单后发送确认邮件" },
          { name: "发货通知", desc: "订单发货后通知客户" },
          { name: "退款通知", desc: "退款处理完成后通知" },
          { name: "营销邮件", desc: "促销活动和新品推荐" },
        ].map((item) => (
          <div key={item.name} className="flex items-center justify-between p-3 border rounded-lg">
            <div>
              <p className="font-medium">{item.name}</p>
              <p className="text-sm text-muted-foreground">{item.desc}</p>
            </div>
            <Switch defaultChecked />
          </div>
        ))}
      </CardContent>
    </Card>
  );
}

// ============ Merchant Notification Settings ============
export function MerchantNotificationSettings() {
  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Bell className="h-5 w-5" />
          商家通知
        </CardTitle>
        <CardDescription>管理店铺推送给商家的通知方式</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        {[
          { name: "新订单通知", desc: "有新订单时立即通知" },
          { name: "低库存预警", desc: "商品库存低于阈值时通知" },
          { name: "退款申请", desc: "收到退款申请时通知" },
        ].map((item) => (
          <div key={item.name} className="flex items-center justify-between p-3 border rounded-lg">
            <div>
              <p className="font-medium">{item.name}</p>
              <p className="text-sm text-muted-foreground">{item.desc}</p>
            </div>
            <Switch defaultChecked />
          </div>
        ))}
      </CardContent>
    </Card>
  );
}

// ============ Tip Settings ============
export function TipSettings() {
  return (
    <Card className="shadow-card">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <DollarSign className="h-5 w-5" />
          小费
        </CardTitle>
        <CardDescription>配置小费，开启后，消费者可在结账页选择是否支付小费，对提升客单价有一定帮助</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <Label>启用小费功能</Label>
            <p className="text-sm text-muted-foreground">结账页面显示小费选项</p>
          </div>
          <Switch />
        </div>
        <div className="space-y-2">
          <Label>预设金额 (¥)</Label>
          <Input placeholder="例如: 2,5,10" defaultValue="2,5,10" />
        </div>
        <div className="flex items-center justify-between">
          <div>
            <Label>允许自定义金额</Label>
            <p className="text-sm text-muted-foreground">客户可输入任意金额</p>
          </div>
          <Switch defaultChecked />
        </div>
      </CardContent>
    </Card>
  );
}
