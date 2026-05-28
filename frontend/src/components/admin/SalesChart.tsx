"use client";

import { Line, Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import type { SalesData } from "@/services/analytics-service";

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface SalesChartProps {
  data: SalesData[];
}

export function SalesChart({ data }: SalesChartProps) {
  const chartData = {
    labels: data.map((d) => {
      const date = new Date(d.date);
      return date.toLocaleDateString("en-US", { month: "short", day: "numeric" });
    }),
    datasets: [
      {
        label: "Revenue",
        data: data.map((d) => d.revenue),
        borderColor: "hsl(222.2 47.4% 11.2%)",
        backgroundColor: "hsla(222.2 47.4% 11.2% / 0.1)",
        fill: true,
        tension: 0.4,
      },
      {
        label: "Orders",
        data: data.map((d) => d.orderCount),
        borderColor: "hsl(142 71% 45%)",
        backgroundColor: "hsla(142 71% 45% / 0.1)",
        fill: true,
        tension: 0.4,
        yAxisID: "y1",
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top" as const,
      },
    },
    scales: {
      y: {
        type: "linear" as const,
        display: true,
        position: "left" as const,
        title: {
          display: true,
          text: "Revenue (¥)",
        },
      },
      y1: {
        type: "linear" as const,
        display: true,
        position: "right" as const,
        grid: {
          drawOnChartArea: false,
        },
        title: {
          display: true,
          text: "Orders",
        },
      },
    },
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Sales Trend (Last 7 Days)</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-[300px]">
          <Line data={chartData} options={options} />
        </div>
      </CardContent>
    </Card>
  );
}

interface TopProductsChartProps {
  data: { productName: string; salesCount: number; revenue: number }[];
}

export function TopProductsChart({ data }: TopProductsChartProps) {
  const chartData = {
    labels: data.map((d) => d.productName.slice(0, 20) + (d.productName.length > 20 ? "..." : "")),
    datasets: [
      {
        label: "Sales",
        data: data.map((d) => d.salesCount),
        backgroundColor: "hsla(222.2 47.4% 11.2% / 0.8)",
        borderRadius: 4,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    indexAxis: "y" as const,
    plugins: {
      legend: {
        display: false,
      },
    },
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Top Selling Products</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-[300px]">
          <Bar data={chartData} options={options} />
        </div>
      </CardContent>
    </Card>
  );
}

interface OrderStatusChartProps {
  data: Record<string, number>;
}

export function OrderStatusChart({ data }: OrderStatusChartProps) {
  const statusColors: Record<string, string> = {
    PENDING_PAYMENT: "hsl(45 93% 47%)",
    PENDING_SHIPMENT: "hsl(221 83% 53%)",
    SHIPPING: "hsl(142 71% 45%)",
    COMPLETED: "hsl(142 71% 45%)",
    CANCELLED: "hsl(0 84% 60%)",
  };

  const chartData = {
    labels: Object.keys(data).map((k) => k.replace("_", " ")),
    datasets: [
      {
        data: Object.values(data),
        backgroundColor: Object.keys(data).map((k) => statusColors[k] || "hsl(0 0% 50%)"),
        borderWidth: 0,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom" as const,
      },
    },
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Order Status Distribution</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-[300px]">
          <Bar data={chartData} options={options} />
        </div>
      </CardContent>
    </Card>
  );
}
