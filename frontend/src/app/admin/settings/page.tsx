"use client";

import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import {
  StoreBasicSettings,
  PaymentSettings,
  ShippingSettings,
  TaxSettings,
  CurrencySettings,
  DomainSettings,
  LanguageSettings,
  CheckoutSettings,
  PolicySettings,
  StaffSettings,
  AuditLogSettings,
  MediaLibrary,
  NotificationSettings,
  MerchantNotificationSettings,
  TipSettings,
} from "@/components/admin/settings/StoreSettings";
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
  History,
  Image,
  Bell,
  DollarSign,
  Settings,
} from "lucide-react";

interface SettingsSection {
  id: string;
  title: string;
  description: string;
  icon: React.ComponentType<{ className?: string }>;
  component: React.ComponentType;
}

const settingsSections: SettingsSection[] = [
  { id: "basic", title: "基本信息", description: "店铺名称、Logo、时区、联系地址", icon: Store, component: StoreBasicSettings },
  { id: "payment", title: "收款", description: "收款方式、PayPal、信用卡等", icon: CreditCard, component: PaymentSettings },
  { id: "shipping", title: "物流配送", description: "配送商品的物流方式", icon: Truck, component: ShippingSettings },
  { id: "tax", title: "税费", description: "根据国家地区设置购物税费", icon: Percent, component: TaxSettings },
  { id: "currency", title: "货币", description: "多种展示货币", icon: Coins, component: CurrencySettings },
  { id: "domain", title: "设置域名", description: "绑定商户自己注册域名", icon: Globe, component: DomainSettings },
  { id: "language", title: "店铺语言", description: "多国语言支持", icon: Globe, component: LanguageSettings },
  { id: "login", title: "客户登录", description: "登录注册方式", icon: Users, component: () => <Card><CardHeader><CardTitle>客户登录</CardTitle><CardDescription>管理店铺客户的登录注册方式</CardDescription></CardHeader><CardContent><p className="text-muted-foreground">配置中...</p></CardContent></Card> },
  { id: "checkout", title: "结账", description: "结账相关流程", icon: ShoppingCart, component: CheckoutSettings },
  { id: "notification", title: "消息通知", description: "邮件通知模板", icon: Bell, component: NotificationSettings },
  { id: "policy", title: "政策条款", description: "店铺政策、条款和协议", icon: Shield, component: PolicySettings },
  { id: "staff", title: "员工账号", description: "员工账号和访问权限", icon: Users, component: StaffSettings },
  { id: "audit", title: "操作日志", description: "员工操作日志记录", icon: History, component: AuditLogSettings },
  { id: "media", title: "素材库", description: "文件和图片管理", icon: Image, component: MediaLibrary },
  { id: "partner", title: "合作伙伴", description: "第三方合作伙伴功能", icon: Settings, component: () => <Card><CardHeader><CardTitle>合作伙伴</CardTitle><CardDescription>授权您的第三方合作伙伴店铺运营相关功能</CardDescription></CardHeader><CardContent><p className="text-muted-foreground">配置中...</p></CardContent></Card> },
  { id: "merchant-notification", title: "商家通知", description: "推送给商家的通知方式", icon: Bell, component: MerchantNotificationSettings },
  { id: "tip", title: "小费", description: "配置小费功能", icon: DollarSign, component: TipSettings },
];

export default function AdminSettingsPage() {
  const [activeSection, setActiveSection] = useState("basic");

  const ActiveComponent = settingsSections.find((s) => s.id === activeSection)?.component || StoreBasicSettings;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">店铺设置</h1>
        <p className="text-muted-foreground">管理您的店铺配置和运营设置</p>
      </div>

      <div className="flex gap-6">
        {/* Sidebar Navigation */}
        <div className="w-64 shrink-0">
          <Card className="shadow-card sticky top-20">
            <CardContent className="p-2">
              <nav className="space-y-1">
                {settingsSections.map((section) => {
                  const Icon = section.icon;
                  return (
                    <button
                      key={section.id}
                      onClick={() => setActiveSection(section.id)}
                      className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                        activeSection === section.id
                          ? "bg-primary/10 text-primary font-medium"
                          : "hover:bg-muted text-muted-foreground"
                      }`}
                    >
                      <Icon className="h-4 w-4" />
                      <span>{section.title}</span>
                    </button>
                  );
                })}
              </nav>
            </CardContent>
          </Card>
        </div>

        {/* Main Content */}
        <div className="flex-1 min-w-0">
          <ActiveComponent />
        </div>
      </div>
    </div>
  );
}
