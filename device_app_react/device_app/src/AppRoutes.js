import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import NotFound from "./pages/NotFound";
import GatewayPage from "./pages/Gateway/GatewayPage";
import GatewayDetails from "./pages/Gateway/GatewayDetails";
import DeviceDetails from "./pages/Device/DeviceDetails";
import DevicePage from "./pages/Device/DevicePage";
import DeviceEditPage from "./pages/Device/DeviceEditPage";
import GatewayEditPage from "./pages/Gateway/GatewayEditPage";
import MessagesPage from "./pages/MessagesPage";

const AppRoutes = () => {
  return (
    <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/gateways" element={<GatewayPage />} />
        <Route path="/gateway/:id" element={<GatewayDetails />} />
        <Route path="/gateway/:id/edit" element={<GatewayEditPage />} />
        <Route path="/devices" element={<DevicePage />} />
        <Route path="/device/:id" element={<DeviceDetails />} />
        <Route path="/device/:id/edit" element={<DeviceEditPage />} />
        <Route path="/messages" element={<MessagesPage />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
  );
}

export default AppRoutes;